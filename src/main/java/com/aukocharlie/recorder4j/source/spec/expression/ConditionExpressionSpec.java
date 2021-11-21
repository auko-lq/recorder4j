package com.aukocharlie.recorder4j.source.spec.expression;

import com.aukocharlie.recorder4j.source.MethodMetadata;
import com.aukocharlie.recorder4j.source.spec.CompilationUnitSpec;
import com.aukocharlie.recorder4j.source.spec.MethodInvocationPlaceableNode;
import com.aukocharlie.recorder4j.source.spec.block.AbstractBlockSpec;
import com.aukocharlie.recorder4j.source.spec.statement.ControlFlow;
import com.sun.jdi.Value;
import com.sun.source.tree.ConditionalExpressionTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author auko
 */
public class ConditionExpressionSpec extends AbstractExpressionSpec implements ControlFlow<Boolean> {

    AbstractExpressionSpec condition;
    AbstractExpressionSpec trueExpression;
    AbstractExpressionSpec falseExpression;

    public ConditionExpressionSpec(ConditionalExpressionTree node, CompilationUnitSpec compilationUnitSpec, String originalExpr) {
        super(originalExpr);
        this.condition = toSpecificExpression(node.getCondition(), compilationUnitSpec);
        this.trueExpression = toSpecificExpression(node.getTrueExpression(), compilationUnitSpec);
        this.falseExpression = toSpecificExpression(node.getFalseExpression(), compilationUnitSpec);
    }

    @Override
    public Boolean evaluateCondition(Map<MethodMetadata, Value> callResults) {
        return null;
    }

    @Override
    public MethodInvocationPlaceableNode nextControlFlowNode() {
        return null;
    }

    @Override
    public List<AbstractBlockSpec> getLambdaBlockList() {
        List<AbstractBlockSpec> lambdaList = new ArrayList<>();
        lambdaList.addAll(condition.getLambdaBlockList());
        lambdaList.addAll(trueExpression.getLambdaBlockList());
        lambdaList.addAll(falseExpression.getLambdaBlockList());
        return lambdaList;
    }

    @Override
    public Expression nextExpression() {
        return null;
    }

    @Override
    protected void setExecutionOrder() {

    }

    @Override
    public void reset() {

    }
}
