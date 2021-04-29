package com.aukocharlie.recorder4j.source.spec;

import com.sun.source.tree.ExpressionStatementTree;

import java.util.List;

/**
 * @author auko
 */
public class ExpressionStatementSpec implements Statement {

    Expression expression;

    public ExpressionStatementSpec(ExpressionStatementTree node, CompilationUnitSpec compilationUnitSpec) {
        this.expression = new ExpressionSpec(node.getExpression(), compilationUnitSpec);
    }

    @Override
    public Expression nextExpression() {
        return null;
    }

    @Override
    public List<BlockSpec> getLambdaBlockList() {
        return expression.getLambdaBlockList();
    }
}
