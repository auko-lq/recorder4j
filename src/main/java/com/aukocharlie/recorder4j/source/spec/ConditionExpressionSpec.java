package com.aukocharlie.recorder4j.source.spec;

import com.aukocharlie.recorder4j.source.UniqueMethod;
import com.sun.jdi.Value;
import com.sun.source.tree.ConditionalExpressionTree;
import com.sun.source.tree.ExpressionTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author auko
 */
public class ConditionExpressionSpec extends ExpressionSpec implements ControlFlowStatement<Boolean> {

    Expression condition;
    Expression thenExpression;
    Expression elseExpression;

    public ConditionExpressionSpec(ConditionalExpressionTree node, CompilationUnitSpec compilationUnitSpec) {
        super(node, compilationUnitSpec);
    }

    @Override
    public Boolean evaluateCondition(Map<UniqueMethod, Value> callResults) {
        return null;
    }

    @Override
    public List<BlockSpec> getLambdaBlockList() {
        List<BlockSpec> lambdaList = new ArrayList<>();
        lambdaList.addAll(condition.getLambdaBlockList());
        lambdaList.addAll(thenExpression.getLambdaBlockList());
        lambdaList.addAll(elseExpression.getLambdaBlockList());
        return lambdaList;
    }

    @Override
    public Expression nextExpression() {
        return null;
    }
}
