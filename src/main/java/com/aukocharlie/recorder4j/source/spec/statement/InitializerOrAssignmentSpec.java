package com.aukocharlie.recorder4j.source.spec.statement;

import com.aukocharlie.recorder4j.source.spec.CompilationUnitSpec;
import com.aukocharlie.recorder4j.source.spec.expression.Expression;
import com.aukocharlie.recorder4j.source.spec.block.AbstractBlockSpec;
import com.aukocharlie.recorder4j.source.spec.expression.AbstractExpressionSpec;
import com.aukocharlie.recorder4j.source.spec.expression.MethodInvocationExpressionSpec;
import com.sun.source.tree.AssignmentTree;
import com.sun.source.tree.VariableTree;

import java.util.List;

/**
 * @author auko
 */
public class InitializerOrAssignmentSpec extends AbstractStatementSpec implements Statement {

    AbstractExpressionSpec valueExpr;

    public InitializerOrAssignmentSpec(VariableTree node, CompilationUnitSpec compilationUnitSpec) {
        this.valueExpr = AbstractExpressionSpec.toSpecificExpression(node.getInitializer(), compilationUnitSpec);
    }

    public InitializerOrAssignmentSpec(AssignmentTree node, CompilationUnitSpec compilationUnitSpec) {
        this.valueExpr = AbstractExpressionSpec.toSpecificExpression(node.getExpression(), compilationUnitSpec);
    }

    @Override
    public Expression nextExpression() {
        return null;
    }

    @Override
    public List<AbstractBlockSpec> getLambdaBlockList() {
        return valueExpr.getLambdaBlockList();
    }

    @Override
    protected void setExecutionOrder() {
        this.nodeInExecutionOrder.add(valueExpr);
    }

    @Override
    public void reset() {

    }
}
