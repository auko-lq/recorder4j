package com.aukocharlie.recorder4j.source.spec.statement;

import com.aukocharlie.recorder4j.source.spec.CompilationUnitSpec;
import com.aukocharlie.recorder4j.source.spec.block.BlockSpec;
import com.aukocharlie.recorder4j.source.spec.block.LoopBlockSpec;
import com.aukocharlie.recorder4j.source.spec.block.MethodBlockSpec;
import com.aukocharlie.recorder4j.source.spec.expression.Expression;
import com.aukocharlie.recorder4j.source.spec.expression.ExpressionSpec;
import com.aukocharlie.recorder4j.source.spec.expression.MethodInvocationExpressionSpec;
import com.sun.source.tree.BreakTree;
import com.sun.source.tree.ReturnTree;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author auko
 */
public class ReturnStatementSpec implements Statement {

    private ExpressionSpec returnValueExpr;

    private final MethodBlockSpec nodeLocatedMethod;

    public ReturnStatementSpec(ReturnTree node, CompilationUnitSpec compilationUnitSpec, MethodBlockSpec nodeLocatedMethod) {
        this.returnValueExpr = ExpressionSpec.toSpecificExpression(node.getExpression(), compilationUnitSpec);
        this.nodeLocatedMethod = nodeLocatedMethod;
    }

    @Override
    public Expression nextExpression() {
        return null;
    }

    @Override
    public List<BlockSpec> getLambdaBlockList() {
        return returnValueExpr.getLambdaBlockList();
    }

    @Override
    public boolean hasNextMethodInvocation() {
        return false;
    }

    @Override
    public MethodInvocationExpressionSpec nextMethodInvocation() {
        throw new NoSuchElementException("There isn't next method invocation");
    }
}
