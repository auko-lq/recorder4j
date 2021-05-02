package com.aukocharlie.recorder4j.source.spec;

import com.aukocharlie.recorder4j.source.MethodInvocationPosition;
import com.aukocharlie.recorder4j.source.Position;
import com.aukocharlie.recorder4j.source.SourcePosition;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.MethodInvocationTree;

import java.util.ArrayList;
import java.util.List;

/**
 * @author auko
 */
public class MethodInvocationExpressionSpec extends ExpressionSpec {

    MethodInvocationExpressionSpec nextMethodInvocationOnChain;

    List<ExpressionSpec> expressionInArgs = new ArrayList<>();

    String methodInvocationSrc;

    SourcePosition methodInvocationPosition;

    CompilationUnitSpec compilationUnitSpec;

    public MethodInvocationExpressionSpec(MethodInvocationTree node, CompilationUnitSpec compilationUnitSpec) {
        this.methodInvocationSrc = node.toString();
        this.methodInvocationPosition = compilationUnitSpec.getSourcePosition(node);
        this.compilationUnitSpec = compilationUnitSpec;
        for (ExpressionTree argument : node.getArguments()) {
            expressionInArgs.add(toSpecificExpression(argument, compilationUnitSpec));
        }
    }

    /**
     * For method chaining. For example:
     * <pre>
     *   <em>test.one().another()</em>
     * </pre>
     *
     * @param wholeChainNode In the example, node is <em>test.one().another()</em>
     * @return the first method invocation on the chain. In the example, it is <em>test.one()</em>
     */
    static MethodInvocationExpressionSpec getFirstMethodInvocationOnChain(MethodInvocationTree wholeChainNode, CompilationUnitSpec compilationUnitSpec) {
        return generateMethodInvocationChain(wholeChainNode, compilationUnitSpec, null);
    }

    static MethodInvocationExpressionSpec generateMethodInvocationChain(MethodInvocationTree currentMethodInvocationNode, CompilationUnitSpec compilationUnitSpec, MethodInvocationExpressionSpec nextMethodInvocation) {
        MethodInvocationExpressionSpec currentMethodInvocation = new MethodInvocationExpressionSpec(currentMethodInvocationNode, compilationUnitSpec);
        if (nextMethodInvocation != null) {
            nextMethodInvocation.adjustSrcAndPosition(currentMethodInvocation);
        }
        currentMethodInvocation.nextMethodInvocationOnChain = nextMethodInvocation;
        ExpressionTree methodSelect = currentMethodInvocationNode.getMethodSelect();
        // TODO: bug fix:  methodSelect is not preMethodInvocation!
        System.out.println(methodSelect);
        System.out.println(methodSelect.getClass());
        if (methodSelect instanceof MethodInvocationTree) {
            return generateMethodInvocationChain((MethodInvocationTree) methodSelect, compilationUnitSpec, currentMethodInvocation);
        } else {
            return currentMethodInvocation;
        }
    }

    @Override
    public List<MethodInvocationPosition> getMethodInvocations() {
        return null;
    }

    @Override
    public List<BlockSpec> getLambdaBlockList() {
        List<BlockSpec> lambdaBlocks = new ArrayList<>();
        expressionInArgs.forEach((expression) -> lambdaBlocks.addAll(expression.getLambdaBlockList()));
        if (nextMethodInvocationOnChain != null) {
            lambdaBlocks.addAll(nextMethodInvocationOnChain.getLambdaBlockList());
        }
        return lambdaBlocks;
    }


    /**
     * Adjust the method invocation source code and position for displaying better.
     * <p>
     * example: chainCase.chain("1").chain("2") -> chain("2")
     */
    private void adjustSrcAndPosition(MethodInvocationExpressionSpec preMethodInvocation) {
        char[] sourceCodeChars = this.compilationUnitSpec.sourceCode.toCharArray();
        SourcePosition preSourcePosition = preMethodInvocation.methodInvocationPosition;
        for (int i = preSourcePosition.endPosition.getPosition(); i < sourceCodeChars.length; i++) {
            if (sourceCodeChars[i] == '.') {
                Position newStartPosition = new Position(
                        compilationUnitSpec.lineMap.getLineNumber(i + 1),
                        compilationUnitSpec.lineMap.getColumnNumber(i + 1),
                        i + 1);
                this.methodInvocationPosition.startPosition = newStartPosition;
                this.methodInvocationPosition.setSource(compilationUnitSpec
                        .sourceCode.substring(newStartPosition.getPosition(), this.methodInvocationPosition.endPosition.getPosition()));
                break;
            }
        }
    }

}
