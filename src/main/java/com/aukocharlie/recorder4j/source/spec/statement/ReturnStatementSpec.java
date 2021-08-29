package com.aukocharlie.recorder4j.source.spec.statement;

import com.aukocharlie.recorder4j.source.spec.CompilationUnitSpec;
import com.aukocharlie.recorder4j.source.spec.block.BlockSpec;
import com.aukocharlie.recorder4j.source.spec.expression.Expression;
import com.aukocharlie.recorder4j.source.spec.expression.ExpressionSpec;
import com.aukocharlie.recorder4j.source.spec.expression.MethodInvocationExpressionSpec;
import com.sun.source.tree.ReturnTree;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author auko
 */
public class ReturnStatementSpec implements Statement {

    private final ExpressionSpec returnValueExpr;

    private final BlockSpec nodeLocatedBlock;

    public ReturnStatementSpec(ReturnTree node, CompilationUnitSpec compilationUnitSpec, BlockSpec nodeLocatedBlock) {
        this.returnValueExpr = ExpressionSpec.toSpecificExpression(node.getExpression(), compilationUnitSpec);
        this.nodeLocatedBlock = nodeLocatedBlock;
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
        if (returnValueExpr.hasNextMethodInvocation()) {
            return true;
        } else {
            nodeLocatedBlock.doReturn();
            return false;
        }
    }

    @Override
    public MethodInvocationExpressionSpec nextMethodInvocation() {
        if (!this.hasNextMethodInvocation()) {
            throw new NoSuchElementException("There isn't next method invocation");
        } else {
            return returnValueExpr.nextMethodInvocation();
        }
    }
}
