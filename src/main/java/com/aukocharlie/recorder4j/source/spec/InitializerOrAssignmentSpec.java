package com.aukocharlie.recorder4j.source.spec;

import com.sun.source.tree.AssignmentTree;
import com.sun.source.tree.StatementTree;
import com.sun.source.tree.VariableTree;

import java.util.List;

/**
 * @author auko
 */
public class InitializerOrAssignmentSpec implements Statement {

    ExpressionSpec valueExpr;

    public InitializerOrAssignmentSpec(VariableTree node, CompilationUnitSpec compilationUnitSpec) {
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
    public MethodInvocationPosition nextMethodInvocation() {
        return null;
    }
}
