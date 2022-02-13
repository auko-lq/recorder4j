package com.aukocharlie.recorder4j.source.spec.statement;

import com.aukocharlie.recorder4j.exception.UnsupportedStatementException;
import com.aukocharlie.recorder4j.source.MethodMetadata;
import com.aukocharlie.recorder4j.source.spec.CompilationUnitSpec;
import com.aukocharlie.recorder4j.source.spec.MethodInvocationPlaceableNode;
import com.aukocharlie.recorder4j.source.spec.expression.Expression;
import com.aukocharlie.recorder4j.source.spec.expression.AbstractExpressionSpec;
import com.aukocharlie.recorder4j.source.spec.block.AbstractBlockSpec;
import com.aukocharlie.recorder4j.source.spec.expression.MethodInvocationExpressionSpec;
import com.sun.jdi.Value;
import com.sun.source.tree.IfTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author auko
 */
public class IfSpec  extends AbstractStatementSpec implements ControlFlow<Boolean> {

    AbstractExpressionSpec condition;
    AbstractBlockSpec thenBlock;
    AbstractBlockSpec elseBlock;

    public IfSpec(IfTree node, CompilationUnitSpec compilationUnitSpec) {
        throw new UnsupportedStatementException("IF statement is not supported now");
//        this.condition = AbstractExpressionSpec.toSpecificExpression(node.getCondition(), compilationUnitSpec);
//        this.thenBlock = new AbstractBlockSpec(node.getThenStatement(), compilationUnitSpec);
//        // TODO：else if ？
//        this.elseBlock = new AbstractBlockSpec(node.getElseStatement(), compilationUnitSpec);
    }

    @Override
    public Boolean evaluateCondition(Map<MethodMetadata, Value> callResults) {
        return null;
    }

    public MethodInvocationPlaceableNode nextControlFlowNode() {
        return null;
    }

    @Override
    public Expression nextExpression() {
        return null;
    }

    @Override
    public List<AbstractBlockSpec> getLambdaBlockList() {
        List<AbstractBlockSpec> lambdaList = new ArrayList<>();
        lambdaList.addAll(condition.getLambdaBlockList());
        lambdaList.addAll(thenBlock.getLambdaBlockList());
        lambdaList.addAll(elseBlock.getLambdaBlockList());
        return lambdaList;
    }

    @Override
    protected void setExecutionOrder() {

    }

    @Override
    public boolean hasNextMethodInvocation() {
        return false;
    }

    @Override
    public MethodInvocationExpressionSpec nextMethodInvocation() {
        return null;
    }

    @Override
    public void reset() {

    }
}
