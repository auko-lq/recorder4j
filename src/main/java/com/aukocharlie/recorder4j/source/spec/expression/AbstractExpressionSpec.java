package com.aukocharlie.recorder4j.source.spec.expression;

import com.aukocharlie.recorder4j.exception.RecorderRuntimeException;
import com.aukocharlie.recorder4j.source.SourceScanner;
import com.aukocharlie.recorder4j.source.scanner.ExpressionScanner;
import com.aukocharlie.recorder4j.source.spec.AbstractMethodInvocationIterator;
import com.aukocharlie.recorder4j.source.spec.CompilationUnitSpec;
import com.aukocharlie.recorder4j.source.spec.block.AbstractBlockSpec;
import com.sun.source.tree.*;
import lombok.Getter;

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
        return Objects.requireNonNullElseGet(expressionScanner.getSpecificExpr(), () -> new BlankExpressionSpec(node.toString()));
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
