package com.aukocharlie.recorder4j.source.spec.statement;

import com.aukocharlie.recorder4j.source.spec.block.BlockSpec;
import com.aukocharlie.recorder4j.source.spec.block.LoopBlockSpec;
import com.aukocharlie.recorder4j.source.spec.expression.Expression;
import com.aukocharlie.recorder4j.source.spec.expression.MethodInvocationExpressionSpec;
import com.sun.source.tree.BreakTree;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author auko
 */
public class BreakStatementSpec implements Statement {

    private final LoopBlockSpec nodeLocatedLoop;

    private final String breakToLabelName;

    public BreakStatementSpec(BreakTree node, LoopBlockSpec nodeLocatedLoop) {
        this.nodeLocatedLoop = nodeLocatedLoop;
        this.breakToLabelName = node.getLabel().toString();
    }

    @Override
    public Expression nextExpression() {
        return null;
    }

    @Override
    public List<BlockSpec> getLambdaBlockList() {
        return Collections.emptyList();
    }

    @Override
    public boolean hasNextMethodInvocation() {
        nodeLocatedLoop.doBreak(breakToLabelName);
        return false;
    }

    @Override
    public MethodInvocationExpressionSpec nextMethodInvocation() {
        throw new NoSuchElementException("There isn't next method invocation");

    }
}
