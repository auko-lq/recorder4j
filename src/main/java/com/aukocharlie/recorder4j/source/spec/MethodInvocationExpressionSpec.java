package com.aukocharlie.recorder4j.source.spec;

import com.aukocharlie.recorder4j.exception.RecorderRuntimeException;
import com.aukocharlie.recorder4j.source.Position;
import com.aukocharlie.recorder4j.source.SourcePosition;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.NewClassTree;

import java.util.ArrayList;
import java.util.List;

/**
 * @author auko
 */
public class MethodInvocationExpressionSpec extends ExpressionSpec {

    MethodInvocationExpressionSpec nextMethodInvocationOnChain;

    List<ExpressionSpec> expressionInArgs = new ArrayList<>();

    MethodInvocationPosition methodInvocationPosition;

    CompilationUnitSpec compilationUnitSpec;

    /**
     * Think of <em>new class</em> as a <em>method invocation</em> (constructor method)
     */
    public MethodInvocationExpressionSpec(ExpressionTree node, CompilationUnitSpec compilationUnitSpec) {
        System.out.println(node);
        this.methodInvocationPosition = new MethodInvocationPosition(compilationUnitSpec.getSourcePosition(node));
        this.compilationUnitSpec = compilationUnitSpec;
        if (node instanceof MethodInvocationTree) {
            for (ExpressionTree argument : ((MethodInvocationTree) node).getArguments()) {
                expressionInArgs.add(toSpecificExpression(argument, compilationUnitSpec));
            }
        } else if (node instanceof NewClassTree) {
            for (ExpressionTree argument : ((NewClassTree) node).getArguments()) {
                expressionInArgs.add(toSpecificExpression(argument, compilationUnitSpec));
            }
        } else {
            throw new RecorderRuntimeException("ExpressionTree must be MethodInvocation or NewClass, not " + node.getKind());
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
    static MethodInvocationExpressionSpec getFirstMethodInvocationOnChain(ExpressionTree wholeChainNode, CompilationUnitSpec compilationUnitSpec) {
        return generateMethodInvocationChain(wholeChainNode, compilationUnitSpec, null);
    }

    static MethodInvocationExpressionSpec generateMethodInvocationChain(ExpressionTree currentMethodInvocationNode, CompilationUnitSpec compilationUnitSpec, MethodInvocationExpressionSpec nextMethodInvocation) {
        MethodInvocationExpressionSpec currentMethodInvocation = new MethodInvocationExpressionSpec(currentMethodInvocationNode, compilationUnitSpec);
        if (nextMethodInvocation != null) {
            nextMethodInvocation.adjustSrcAndPosition(currentMethodInvocation);
        }
        currentMethodInvocation.nextMethodInvocationOnChain = nextMethodInvocation;

        // Try to get previous method invocation on method chaining.
        // Note: NewClass node must be at the head of method chaining.
        if (currentMethodInvocationNode instanceof MethodInvocationTree) {
            ExpressionTree methodSelect = ((MethodInvocationTree) currentMethodInvocationNode).getMethodSelect();
            if (methodSelect instanceof MemberSelectTree) {
                ExpressionTree preExpression = ((MemberSelectTree) methodSelect).getExpression();
                if (preExpression instanceof MethodInvocationTree || preExpression instanceof NewClassTree) {
                    return generateMethodInvocationChain(preExpression, compilationUnitSpec, currentMethodInvocation);
                }
            }
        }

        return currentMethodInvocation;
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
                this.methodInvocationPosition.source = compilationUnitSpec
                        .sourceCode.substring(newStartPosition.getPosition(), this.methodInvocationPosition.endPosition.getPosition());
                break;
            }
        }
    }

}

class MethodInvocationPosition extends SourcePosition {

    public MethodInvocationPosition(SourcePosition sourcePosition) {
        super(sourcePosition.startPosition, sourcePosition.endPosition, sourcePosition.source);
    }

}

