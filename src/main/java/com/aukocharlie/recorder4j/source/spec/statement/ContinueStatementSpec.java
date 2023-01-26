package com.aukocharlie.recorder4j.source.spec.statement;

import com.aukocharlie.recorder4j.source.spec.expression.Expression;
import com.aukocharlie.recorder4j.source.spec.block.LoopBlockSpec;
import com.aukocharlie.recorder4j.source.spec.block.AbstractBlockSpec;
import com.aukocharlie.recorder4j.source.spec.expression.MethodInvocationExpressionSpec;
import com.sun.source.tree.ContinueTree;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author auko
 */
public class ContinueStatementSpec extends AbstractStatementSpec {

    private final LoopBlockSpec nodeLocatedLoop;

    private final String continueToLabelName;

    public ContinueStatementSpec(ContinueTree node, LoopBlockSpec nodeLocatedBlock) {
        this.nodeLocatedLoop = nodeLocatedBlock;
        this.continueToLabelName = node.getLabel() == null ? null : node.getLabel().toString();
    }

    @Override
    public Expression nextExpression() {
        return null;
    }

    @Override
    public List<AbstractBlockSpec> getLambdaBlockList() {
        return Collections.emptyList();
    }

    @Override
    protected void setExecutionOrder() {
    }

    @Override
    public boolean hasNextMethodInvocation() {
        nodeLocatedLoop.doContinue(continueToLabelName);
        return false;
    }

    @Override
    public MethodInvocationExpressionSpec nextMethodInvocation() {
        throw new NoSuchElementException("There isn't next method invocation");
    }

    @Override
    public void reset() {

    }
}
