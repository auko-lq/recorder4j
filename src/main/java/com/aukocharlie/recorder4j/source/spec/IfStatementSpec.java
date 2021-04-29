package com.aukocharlie.recorder4j.source.spec;

import com.aukocharlie.recorder4j.source.UniqueMethod;
import com.sun.jdi.Value;
import com.sun.source.tree.IfTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author auko
 */
public class IfStatementSpec implements ControlFlowStatement<Boolean> {

    Expression condition;
    BlockSpec thenBlock;
    BlockSpec elseBlock;

    public IfStatementSpec(IfTree node, CompilationUnitSpec compilationUnitSpec) {
        this.condition = new ExpressionSpec(node.getCondition(), compilationUnitSpec);
        this.thenBlock = new BlockSpec(node.getThenStatement(), compilationUnitSpec);
        this.elseBlock = new BlockSpec(node.getElseStatement(), compilationUnitSpec);
    }

    @Override
    public Boolean evaluateCondition(Map<UniqueMethod, Value> callResults) {
        return null;
    }

    @Override
    public Expression nextExpression() {
        return null;
    }

    @Override
    public List<BlockSpec> getLambdaBlockList() {
        List<BlockSpec> lambdaList = new ArrayList<>();
        lambdaList.addAll(condition.getLambdaBlockList());
        lambdaList.addAll(thenBlock.getLambdaBlockList());
        lambdaList.addAll(elseBlock.getLambdaBlockList());
        return lambdaList;
    }
}
