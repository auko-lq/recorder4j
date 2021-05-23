package com.aukocharlie.recorder4j.source.spec;

import com.sun.source.tree.BreakTree;

import java.util.List;

/**
 * @author auko
 */
public class BreakStatementSpec implements Statement {

    public BreakStatementSpec(BreakTree node, LoopBlockSpec nodeLocatedLoop) {
        nodeLocatedLoop.doBreak(node.getLabel().toString());
    }

    @Override
    public Expression nextExpression() {
        return null;
    }

    @Override
    public List<BlockSpec> getLambdaBlockList() {
        return null;
    }

    @Override
    public boolean hasNextMethodInvocation() {
        return false;
    }

    @Override
    public MethodInvocationPosition nextMethodInvocation() {
        return null;
    }
}
