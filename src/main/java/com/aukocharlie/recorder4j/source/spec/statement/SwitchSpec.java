package com.aukocharlie.recorder4j.source.spec.statement;

import com.aukocharlie.recorder4j.source.MethodMetadata;
import com.aukocharlie.recorder4j.source.spec.CompilationUnitSpec;
import com.aukocharlie.recorder4j.source.spec.expression.Expression;
import com.aukocharlie.recorder4j.source.spec.expression.AbstractExpressionSpec;
import com.aukocharlie.recorder4j.source.spec.block.AbstractBlockSpec;
import com.aukocharlie.recorder4j.source.spec.expression.MethodInvocationExpressionSpec;
import com.sun.jdi.Value;
import com.sun.source.tree.CaseTree;
import com.sun.source.tree.SwitchTree;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: support switch statement
 * @author auko
 */
public class SwitchSpec extends AbstractStatementSpec implements ControlFlow<Integer> {

    AbstractExpressionSpec condition;

    /**
     * Use LinkedHashMap to ensure the order between cases
     */
    Map<AbstractExpressionSpec, AbstractBlockSpec> cases = new LinkedHashMap<>();

    public SwitchSpec(SwitchTree node, CompilationUnitSpec compilationUnitSpec) {
        this.condition = AbstractExpressionSpec.toSpecificExpression(node.getExpression(), compilationUnitSpec);
        List<? extends CaseTree> cases = node.getCases();
        if (cases != null) {
            for (CaseTree caseItem : cases) {
                this.cases.put(
                        AbstractExpressionSpec.toSpecificExpression(node.getExpression(), compilationUnitSpec),
                        new AbstractBlockSpec(caseItem.getStatements(), compilationUnitSpec));
            }
        }
    }

    @Override
    public Integer evaluateCondition(Map<MethodMetadata, Value> callResults) {
        return null;
    }

    @Override
    public List<AbstractBlockSpec> getLambdaBlockList() {
        List<AbstractBlockSpec> lambdaList = new ArrayList<>();
        lambdaList.addAll(condition.getLambdaBlockList());
        cases.values().forEach((caseItem) -> lambdaList.addAll(caseItem.getLambdaBlockList()));
        return lambdaList;
    }

    @Override
    public Expression nextExpression() {
        return null;
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
