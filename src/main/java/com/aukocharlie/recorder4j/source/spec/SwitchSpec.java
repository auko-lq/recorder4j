package com.aukocharlie.recorder4j.source.spec;

import com.aukocharlie.recorder4j.source.UniqueMethod;
import com.sun.jdi.Value;
import com.sun.source.tree.CaseTree;
import com.sun.source.tree.SwitchTree;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author auko
 */
public class SwitchSpec implements ControlFlow<Integer> {

    ExpressionSpec condition;

    /**
     * Use LinkedHashMap to ensure the order between cases
     */
    Map<ExpressionSpec, BlockSpec> cases = new LinkedHashMap<>();

    public SwitchSpec(SwitchTree node, CompilationUnitSpec compilationUnitSpec) {
        this.condition = ExpressionSpec.toSpecificExpression(node.getExpression(), compilationUnitSpec);
        List<? extends CaseTree> cases = node.getCases();
        if (cases != null) {
            for (CaseTree caseItem : cases) {
                this.cases.put(
                        ExpressionSpec.toSpecificExpression(node.getExpression(), compilationUnitSpec),
                        new BlockSpec(caseItem.getStatements(), compilationUnitSpec));
            }
        }
    }

    @Override
    public Integer evaluateCondition(Map<UniqueMethod, Value> callResults) {
        return null;
    }

    @Override
    public List<BlockSpec> getLambdaBlockList() {
        List<BlockSpec> lambdaList = new ArrayList<>();
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
    public MethodInvocationPosition nextMethodInvocation() {
        return null;
    }
}
