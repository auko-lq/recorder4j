package com.aukocharlie.recorder4j.source.spec.expression;

import com.aukocharlie.recorder4j.source.MethodMetadata;
import com.aukocharlie.recorder4j.source.spec.CompilationUnitSpec;
import com.aukocharlie.recorder4j.source.spec.MethodInvocationPlaceableNode;
import com.aukocharlie.recorder4j.source.spec.block.AbstractBlockSpec;
import com.aukocharlie.recorder4j.source.spec.statement.ControlFlow;
import com.sun.jdi.Value;
import com.sun.source.tree.BinaryTree;
import com.sun.source.tree.Tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author auko
 */
public class BinaryExpressionSpec extends AbstractExpressionSpec implements ControlFlow<Boolean> {

    AbstractExpressionSpec leftOperand;
    Tree.Kind operator;
    AbstractExpressionSpec rightOperand;

    public BinaryExpressionSpec(BinaryTree node, CompilationUnitSpec compilationUnitSpec, String originalExpr) {
        super(originalExpr);
        this.leftOperand = toSpecificExpression(node.getLeftOperand(), compilationUnitSpec);
        this.operator = node.getKind();
        this.rightOperand = toSpecificExpression(node.getRightOperand(), compilationUnitSpec);
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
    public Expression nextExpression() {
        return null;
    }

    @Override
    protected void setExecutionOrder() {

    }

    @Override
    public List<AbstractBlockSpec> getLambdaBlockList() {
        List<AbstractBlockSpec> lambdaList = new ArrayList<>(2);
        lambdaList.addAll(leftOperand.getLambdaBlockList());
        lambdaList.addAll(rightOperand.getLambdaBlockList());
        return lambdaList;
    }

    @Override
    public void reset() {

    }
}
