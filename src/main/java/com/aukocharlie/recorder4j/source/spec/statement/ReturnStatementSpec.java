package com.aukocharlie.recorder4j.source.spec.statement;

import com.aukocharlie.recorder4j.source.spec.CompilationUnitSpec;
import com.aukocharlie.recorder4j.source.spec.block.AbstractBlockSpec;
import com.aukocharlie.recorder4j.source.spec.expression.Expression;
import com.aukocharlie.recorder4j.source.spec.expression.AbstractExpressionSpec;
import com.aukocharlie.recorder4j.source.spec.expression.MethodInvocationExpressionSpec;
import com.sun.source.tree.ReturnTree;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author auko
 */
public class ReturnStatementSpec extends AbstractStatementSpec implements Statement {

    private final AbstractExpressionSpec returnValueExpr;

    private final AbstractBlockSpec nodeLocatedBlock;

    public ReturnStatementSpec(ReturnTree node, CompilationUnitSpec compilationUnitSpec, AbstractBlockSpec nodeLocatedBlock) {
        this.returnValueExpr = AbstractExpressionSpec.toSpecificExpression(node.getExpression(), compilationUnitSpec);
        this.nodeLocatedBlock = nodeLocatedBlock;
    }

    @Override
    public Expression nextExpression() {
        return null;
    }

    @Override
    public List<AbstractBlockSpec> getLambdaBlockList() {
        return returnValueExpr.getLambdaBlockList();
    }

    @Override
    protected void setExecutionOrder() {

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
        if (!returnValueExpr.hasNextMethodInvocation()) {
            throw new NoSuchElementException("There isn't next method invocation");
        } else {
            return returnValueExpr.nextMethodInvocation();
        }
    }

    @Override
    public void reset() {

    }
}
