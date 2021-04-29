package com.aukocharlie.recorder4j.source.spec;

import com.aukocharlie.recorder4j.source.UniqueMethod;
import com.sun.jdi.Value;
import com.sun.source.tree.CaseTree;
import com.sun.source.tree.SwitchTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author auko
 */
public class SwitchStatementSpec implements ControlFlowStatement<Integer> {

    Expression condition;
    List<BlockSpec> cases = new ArrayList<>();

    public SwitchStatementSpec(SwitchTree node, CompilationUnitSpec compilationUnitSpec) {
        this.condition = new ExpressionSpec(node.getExpression(), compilationUnitSpec);
        List<? extends CaseTree> cases = node.getCases();
        if (cases != null) {
            for (CaseTree caseItem : cases) {
                this.cases.add(new BlockSpec(caseItem.getStatements(), compilationUnitSpec));
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
        cases.forEach((caseItem) -> lambdaList.addAll(caseItem.getLambdaBlockList()));
        return lambdaList;
    }

    @Override
    public Expression nextExpression() {
        return null;
    }
}
