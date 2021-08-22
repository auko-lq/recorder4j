package com.aukocharlie.recorder4j.source.spec.statement;

import com.aukocharlie.recorder4j.source.spec.*;
import com.aukocharlie.recorder4j.source.spec.block.BlockSpec;
import com.aukocharlie.recorder4j.source.spec.expression.Expression;
import com.aukocharlie.recorder4j.source.spec.expression.ExpressionSpec;
import com.aukocharlie.recorder4j.source.spec.expression.MethodInvocationExpressionSpec;
import com.sun.source.tree.ExpressionStatementTree;

import java.util.List;

/**
 * @author auko
 */
public class ExpressionStatementSpec implements Statement {

    ExpressionSpec expression;

    public ExpressionStatementSpec(ExpressionStatementTree node, CompilationUnitSpec compilationUnitSpec) {
        this.expression = ExpressionSpec.toSpecificExpression(node.getExpression(), compilationUnitSpec);
    }

    @Override
    public Expression nextExpression() {
        return null;
    }

    @Override
    public List<BlockSpec> getLambdaBlockList() {
        return expression.getLambdaBlockList();
    }

    @Override
    public boolean hasNextMethodInvocation() {
        return false;
    }

    @Override
    public MethodInvocationExpressionSpec nextMethodInvocation() {
        return null;
    }
}
