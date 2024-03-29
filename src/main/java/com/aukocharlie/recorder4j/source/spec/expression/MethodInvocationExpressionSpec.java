package com.aukocharlie.recorder4j.source.spec.expression;

import com.aukocharlie.recorder4j.exception.RecorderRuntimeException;
import com.aukocharlie.recorder4j.source.Position;
import com.aukocharlie.recorder4j.source.SourcePosition;
import com.aukocharlie.recorder4j.source.spec.CompilationUnitSpec;
import com.aukocharlie.recorder4j.source.spec.MethodInvocationPosition;
import com.aukocharlie.recorder4j.source.spec.block.AbstractBlockSpec;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.NewClassTree;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author auko
 */
public class MethodInvocationExpressionSpec extends AbstractExpressionSpec {

    MethodInvocationExpressionSpec nextMethodInvocationOnChain;

    List<AbstractExpressionSpec> expressionInArgs = new ArrayList<>();

    @Getter
    MethodInvocationPosition methodInvocationPosition;

    CompilationUnitSpec compilationUnitSpec;

    @Getter
    @Setter
    private boolean scanned = false;

    @Override
    public List<AbstractBlockSpec> getLambdaBlockList() {
        List<AbstractBlockSpec> lambdaBlocks = new ArrayList<>();
        expressionInArgs.forEach((expression) -> lambdaBlocks.addAll(expression.getLambdaBlockList()));
        if (nextMethodInvocationOnChain != null) {
            lambdaBlocks.addAll(nextMethodInvocationOnChain.getLambdaBlockList());
        }
        return lambdaBlocks;
    }

    @Override
    protected void setExecutionOrder() {
        this.nodeInExecutionOrder.addAll(expressionInArgs);
        this.nodeInExecutionOrder.add(this);
        if (nextMethodInvocationOnChain != null) {
            this.nodeInExecutionOrder.add(nextMethodInvocationOnChain);
        }
    }

    @Override
    public void reset() {

    }

    /**
     * Think of <em>new class</em> as a <em>method invocation</em> (constructor method)
     */
    public MethodInvocationExpressionSpec(ExpressionTree node, CompilationUnitSpec compilationUnitSpec, String originalExpr) {
        super(originalExpr);
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
    public static MethodInvocationExpressionSpec getFirstMethodInvocationOnChain(ExpressionTree wholeChainNode, CompilationUnitSpec compilationUnitSpec, String originalExpr) {
        return generateMethodInvocationChain(wholeChainNode, compilationUnitSpec, null, originalExpr);
    }

    static MethodInvocationExpressionSpec generateMethodInvocationChain(
            ExpressionTree currentMethodInvocationNode,
            CompilationUnitSpec compilationUnitSpec,
            MethodInvocationExpressionSpec nextMethodInvocation,
            String originalExpr) {

        MethodInvocationExpressionSpec currentMethodInvocation = new MethodInvocationExpressionSpec(currentMethodInvocationNode, compilationUnitSpec, originalExpr);
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
                    return generateMethodInvocationChain(preExpression, compilationUnitSpec, currentMethodInvocation, originalExpr);
                }
            }
        }

        return currentMethodInvocation;
    }

    /**
     * Adjust the method invocation source code and position for displaying better.
     * <p>
     * example: chainCase.chain("1").chain("2") -> chain("2")
     */
    private void adjustSrcAndPosition(MethodInvocationExpressionSpec preMethodInvocation) {
        char[] sourceCodeChars = this.compilationUnitSpec.getSourceCode().toCharArray();
        SourcePosition preSourcePosition = preMethodInvocation.methodInvocationPosition;
        for (int i = preSourcePosition.endPosition.getPosition(); i < sourceCodeChars.length; i++) {
            if (sourceCodeChars[i] == '.') {
                Position newStartPosition = new Position(
                        compilationUnitSpec.getLineMap().getLineNumber(i + 1),
                        compilationUnitSpec.getLineMap().getColumnNumber(i + 1),
                        i + 1);
                this.methodInvocationPosition.startPosition = newStartPosition;
                this.methodInvocationPosition.source = compilationUnitSpec
                        .getSourceCode().substring(newStartPosition.getPosition(), this.methodInvocationPosition.endPosition.getPosition());
                break;
            }
        }
    }

}

