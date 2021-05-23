package com.aukocharlie.recorder4j.source.spec;

import com.sun.source.tree.ContinueTree;

import java.util.List;

/**
 * @author auko
 */
public class ContinueStatementSpec implements Statement {

    public ContinueStatementSpec(ContinueTree node, LoopBlockSpec nodeLocatedBlock) {
        nodeLocatedBlock.doContinue(node.getLabel().toString());
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
