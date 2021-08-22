package com.aukocharlie.recorder4j.source.spec.expression;

import com.aukocharlie.recorder4j.source.UniqueMethod;
import com.aukocharlie.recorder4j.source.spec.CompilationUnitSpec;
import com.aukocharlie.recorder4j.source.spec.block.BlockSpec;
import com.aukocharlie.recorder4j.source.spec.statement.ControlFlow;
import com.sun.jdi.Value;
import com.sun.source.tree.ConditionalExpressionTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author auko
 */
public class ConditionExpressionSpec extends ExpressionSpec implements ControlFlow<Boolean> {

    ExpressionSpec condition;
    ExpressionSpec trueExpression;
    ExpressionSpec falseExpression;

    public ConditionExpressionSpec(ConditionalExpressionTree node, CompilationUnitSpec compilationUnitSpec, String originalExpr) {
        super(originalExpr);
        this.condition = toSpecificExpression(node.getCondition(), compilationUnitSpec);
        this.trueExpression = toSpecificExpression(node.getTrueExpression(), compilationUnitSpec);
        this.falseExpression = toSpecificExpression(node.getFalseExpression(), compilationUnitSpec);
    }

    @Override
    public Boolean evaluateCondition(Map<UniqueMethod, Value> callResults) {
        return null;
    }

    @Override
    public List<BlockSpec> getLambdaBlockList() {
        List<BlockSpec> lambdaList = new ArrayList<>();
        lambdaList.addAll(condition.getLambdaBlockList());
        lambdaList.addAll(trueExpression.getLambdaBlockList());
        lambdaList.addAll(falseExpression.getLambdaBlockList());
        return lambdaList;
    }

    @Override
    public Expression nextExpression() {
        return null;
    }
}
