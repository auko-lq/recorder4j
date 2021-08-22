package com.aukocharlie.recorder4j.source.spec.statement;

import com.aukocharlie.recorder4j.source.spec.CompilationUnitSpec;
import com.aukocharlie.recorder4j.source.spec.expression.Expression;
import com.aukocharlie.recorder4j.source.spec.block.BlockSpec;
import com.aukocharlie.recorder4j.source.spec.expression.ExpressionSpec;
import com.aukocharlie.recorder4j.source.spec.expression.MethodInvocationExpressionSpec;
import com.sun.source.tree.AssignmentTree;
import com.sun.source.tree.VariableTree;

import java.util.List;

/**
 * @author auko
 */
public class InitializerOrAssignmentSpec implements Statement {

    ExpressionSpec valueExpr;

    public InitializerOrAssignmentSpec(VariableTree node, CompilationUnitSpec compilationUnitSpec) {
        System.out.println("[debug] " + node);
        this.valueExpr = ExpressionSpec.toSpecificExpression(node.getInitializer(), compilationUnitSpec);
    }

    public InitializerOrAssignmentSpec(AssignmentTree node, CompilationUnitSpec compilationUnitSpec) {
        this.valueExpr = ExpressionSpec.toSpecificExpression(node.getExpression(), compilationUnitSpec);
    }

    @Override
    public Expression nextExpression() {
        return null;
    }

    @Override
    public List<BlockSpec> getLambdaBlockList() {
        return valueExpr.getLambdaBlockList();
    }

    @Override
    public boolean hasNextMethodInvocation() {
        return false;
    }

    @Override
    public MethodInvocationExpressionSpec nextMethodInvocation() {
        return null;
    }
}
