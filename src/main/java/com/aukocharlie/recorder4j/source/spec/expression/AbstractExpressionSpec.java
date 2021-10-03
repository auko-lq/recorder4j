package com.aukocharlie.recorder4j.source.spec.expression;

import com.aukocharlie.recorder4j.exception.RecorderRuntimeException;
import com.aukocharlie.recorder4j.source.SourceScanner;
import com.aukocharlie.recorder4j.source.spec.AbstractMethodInvocationIterator;
import com.aukocharlie.recorder4j.source.spec.CompilationUnitSpec;
import com.aukocharlie.recorder4j.source.spec.block.AbstractBlockSpec;
import com.sun.source.tree.*;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import static com.aukocharlie.recorder4j.source.spec.expression.MethodInvocationExpressionSpec.getFirstMethodInvocationOnChain;
import static com.sun.source.tree.LambdaExpressionTree.BodyKind.STATEMENT;

/**
 * @author auko
 */
public abstract class AbstractExpressionSpec extends AbstractMethodInvocationIterator implements Expression {

    String originalExpr;

    public AbstractExpressionSpec(String expr) {
        this.originalExpr = expr;
    }

    /**
     * Perform a depth-first search on expression to find the specific expression scanned first.
     */
    public static AbstractExpressionSpec toSpecificExpression(ExpressionTree node, CompilationUnitSpec compilationUnitSpec) {
        if (Objects.isNull(node)) {
            return new BlankExpressionSpec(null);
        }

        ExpressionScanner expressionScanner = new ExpressionScanner(node.toString());
        expressionScanner.scan(node, compilationUnitSpec);
        if (expressionScanner.specificExpr != null) {
            return expressionScanner.specificExpr;
        }
        return new BlankExpressionSpec(node.toString());
    }

    static class ExpressionScanner extends SourceScanner {

        AbstractExpressionSpec specificExpr = null;

        String scannedExpr;

        public ExpressionScanner(String expr) {
            this.scannedExpr = expr;
        }

        @Override
        public Void visitLambdaExpression(LambdaExpressionTree node, CompilationUnitSpec compilationUnitSpec) {
            if (this.specificExpr == null && node.getBodyKind() == STATEMENT) {
                this.specificExpr = new LambdaExpressionSpec(node, compilationUnitSpec, scannedExpr);
            }
            return null;
        }

        @Override
        public Void visitConditionalExpression(ConditionalExpressionTree node, CompilationUnitSpec compilationUnitSpec) {
            if (this.specificExpr == null) {
                this.specificExpr = new ConditionExpressionSpec(node, compilationUnitSpec, scannedExpr);
            }
            return null;
        }

        @Override
        public Void visitBinary(BinaryTree node, CompilationUnitSpec compilationUnitSpec) {
            if (this.specificExpr == null) {
                this.specificExpr = new BinaryExpressionSpec(node, compilationUnitSpec, scannedExpr);
            }
            return null;
        }

        @Override
        public Void visitMethodInvocation(MethodInvocationTree node, CompilationUnitSpec compilationUnitSpec) {
            if (this.specificExpr == null) {
                this.specificExpr = getFirstMethodInvocationOnChain(node, compilationUnitSpec, scannedExpr);
            }
            return null;
        }


        /**
         * Think of <em>new class</em> as a <em>method invocation</em> (constructor method)
         */
        @Override
        public Void visitNewClass(NewClassTree node, CompilationUnitSpec compilationUnitSpec) {
            if (this.specificExpr == null) {
                this.specificExpr = getFirstMethodInvocationOnChain(node, compilationUnitSpec, scannedExpr);
            }
            return null;
        }
    }

    static class BlankExpressionSpec extends AbstractExpressionSpec {

        public BlankExpressionSpec(String expr) {
            super(expr);
        }

        @Override
        public List<AbstractBlockSpec> getLambdaBlockList() {
            return Collections.emptyList();
        }

        @Override
        public boolean hasNextMethodInvocation() {
            return false;
        }

        @Override
        public MethodInvocationExpressionSpec nextMethodInvocation() {
            throw new NoSuchElementException();
        }

        @Override
        public void reset() {
        }

        @Override
        protected void setExecutionOrder() {
        }
    }

}
