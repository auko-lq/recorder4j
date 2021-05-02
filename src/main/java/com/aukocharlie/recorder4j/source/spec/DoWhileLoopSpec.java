package com.aukocharlie.recorder4j.source.spec;

import com.aukocharlie.recorder4j.source.UniqueMethod;
import com.sun.jdi.Value;
import com.sun.source.tree.DoWhileLoopTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author auko
 */
public class DoWhileLoopSpec implements ControlFlow<Boolean> {

    BlockSpec loopBlock;
    ExpressionSpec condition;

    public DoWhileLoopSpec(DoWhileLoopTree node, CompilationUnitSpec compilationUnitSpec) {
        this.loopBlock = new BlockSpec(node.getStatement(), compilationUnitSpec);
        this.condition = ExpressionSpec.toSpecificExpression(node.getCondition(), compilationUnitSpec);
    }

    @Override
    public Boolean evaluateCondition(Map<UniqueMethod, Value> callResults) {
        return null;
    }


    @Override
    public List<BlockSpec> getLambdaBlockList() {
        List<BlockSpec> lambdaList = new ArrayList<>();
        lambdaList.addAll(loopBlock.getLambdaBlockList());
        lambdaList.addAll(condition.getLambdaBlockList());
        return lambdaList;
    }

    @Override
    public Expression nextExpression() {
        return null;
    }
}
