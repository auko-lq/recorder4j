package com.aukocharlie.recorder4j.source.spec.expression;

import com.aukocharlie.recorder4j.source.UniqueMethod;
import com.aukocharlie.recorder4j.source.spec.CompilationUnitSpec;
import com.aukocharlie.recorder4j.source.spec.statement.ControlFlow;
import com.sun.jdi.Value;
import com.sun.source.tree.BinaryTree;
import com.sun.source.tree.Tree;

import java.util.Map;

/**
 * @author auko
 */
public class BinaryExpressionSpec extends ExpressionSpec implements ControlFlow<Boolean> {

    ExpressionSpec leftOperand;
    Tree.Kind operator;
    ExpressionSpec rightOperand;

    public BinaryExpressionSpec(BinaryTree node, CompilationUnitSpec compilationUnitSpec, String originalExpr) {
        super(originalExpr);
        this.leftOperand = toSpecificExpression(node.getLeftOperand(), compilationUnitSpec);
        this.operator = node.getKind();
        this.rightOperand = toSpecificExpression(node.getRightOperand(), compilationUnitSpec);
    }

    @Override
    public Boolean evaluateCondition(Map<UniqueMethod, Value> callResults) {
        return null;
    }

    @Override
    public Expression nextExpression() {
        return null;
    }
}
