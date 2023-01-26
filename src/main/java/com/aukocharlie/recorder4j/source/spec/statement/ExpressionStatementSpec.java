package com.aukocharlie.recorder4j.source.spec.statement;

import com.aukocharlie.recorder4j.source.spec.*;
import com.aukocharlie.recorder4j.source.spec.block.AbstractBlockSpec;
import com.aukocharlie.recorder4j.source.spec.expression.Expression;
import com.aukocharlie.recorder4j.source.spec.expression.AbstractExpressionSpec;
import com.aukocharlie.recorder4j.source.spec.expression.MethodInvocationExpressionSpec;
import com.sun.source.tree.ExpressionStatementTree;

import java.util.List;

/**
 * @author auko
 */
public class ExpressionStatementSpec extends AbstractStatementSpec {

    AbstractExpressionSpec expression;

    public ExpressionStatementSpec(ExpressionStatementTree node, CompilationUnitSpec compilationUnitSpec) {
        this.expression = AbstractExpressionSpec.toSpecificExpression(node.getExpression(), compilationUnitSpec);
    }

    @Override
    public Expression nextExpression() {
        return null;
    }

    @Override
    public List<AbstractBlockSpec> getLambdaBlockList() {
        return expression.getLambdaBlockList();
    }

    @Override
    protected void setExecutionOrder() {

    }

    @Override
    public boolean hasNextMethodInvocation() {
        return expression.hasNextMethodInvocation();
    }

    @Override
    public MethodInvocationExpressionSpec nextMethodInvocation() {
        return expression.nextMethodInvocation();
    }

    @Override
    public void reset() {

    }
}
