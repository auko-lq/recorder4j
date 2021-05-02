package com.aukocharlie.recorder4j.source.spec;

import com.aukocharlie.recorder4j.source.MethodInvocationPosition;
import com.aukocharlie.recorder4j.source.SourceScanner;
import com.sun.source.tree.*;

import java.util.Collections;
import java.util.List;

import static com.aukocharlie.recorder4j.source.spec.MethodInvocationExpressionSpec.getFirstMethodInvocationOnChain;
import static com.sun.source.tree.LambdaExpressionTree.BodyKind.STATEMENT;

/**
 * @author auko
 */
public class ExpressionSpec implements Expression {

    private static final ExpressionSpec USELESS_EXPRESSION = new ExpressionSpec();

    /**
     * Perform a depth-first search on expression to find the specific expression scanned first.
     */
    static ExpressionSpec toSpecificExpression(ExpressionTree node, CompilationUnitSpec compilationUnitSpec) {
        ExpressionScanner expressionScanner = new ExpressionScanner();
        expressionScanner.scan(node, compilationUnitSpec);
        if (expressionScanner.specificExpr != null) {
            return expressionScanner.specificExpr;
        }
        return USELESS_EXPRESSION;
    }

    @Override
    public List<MethodInvocationPosition> getMethodInvocations() {
        return null;
    }

    @Override
    public List<BlockSpec> getLambdaBlockList() {
        return Collections.emptyList();
    }

    static class ExpressionScanner extends SourceScanner {

        ExpressionSpec specificExpr = null;

        @Override
        public Void visitLambdaExpression(LambdaExpressionTree node, CompilationUnitSpec compilationUnitSpec) {
            // TODO: body kind is EXPRESSION?
            if (this.specificExpr == null && node.getBodyKind() == STATEMENT) {
                this.specificExpr = new LambdaExpressionSpec(node, compilationUnitSpec);
            }
            return null;
        }

        @Override
        public Void visitConditionalExpression(ConditionalExpressionTree node, CompilationUnitSpec compilationUnitSpec) {
            if (this.specificExpr == null) {
                this.specificExpr = new ConditionExpressionSpec(node, compilationUnitSpec);
            }
            return null;
        }

        @Override
        public Void visitBinary(BinaryTree node, CompilationUnitSpec compilationUnitSpec) {
            if (this.specificExpr == null) {
                this.specificExpr = new BinaryExpressionSpec(node, compilationUnitSpec);
            }
            return null;
        }

        @Override
        public Void visitMethodInvocation(MethodInvocationTree node, CompilationUnitSpec compilationUnitSpec) {
            if (this.specificExpr == null) {
                this.specificExpr = getFirstMethodInvocationOnChain(node, compilationUnitSpec);
            }
            return null;
        }

    }
}
