package com.aukocharlie.recorder4j.source.spec;

import com.aukocharlie.recorder4j.source.UniqueMethod;
import com.sun.jdi.Value;
import com.sun.source.tree.WhileLoopTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author auko
 */
public class WhileLoopSpec implements ControlFlow<Boolean> {

    ExpressionSpec condition;
    BlockSpec loopBlock;

    public WhileLoopSpec(WhileLoopTree node, CompilationUnitSpec compilationUnitSpec) {
        this.condition = ExpressionSpec.toSpecificExpression(node.getCondition(), compilationUnitSpec);
        this.loopBlock = new BlockSpec(node.getStatement(), compilationUnitSpec);
    }

    @Override
    public Boolean evaluateCondition(Map<UniqueMethod, Value> callResults) {
        return null;
    }


    @Override
    public List<BlockSpec> getLambdaBlockList() {
        List<BlockSpec> lambdaList = new ArrayList<>();
        lambdaList.addAll(condition.getLambdaBlockList());
        lambdaList.addAll(loopBlock.getLambdaBlockList());
        return lambdaList;
    }

    @Override
    public Expression nextExpression() {
        return null;
    }
}
