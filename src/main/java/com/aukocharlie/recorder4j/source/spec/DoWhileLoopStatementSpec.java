package com.aukocharlie.recorder4j.source.spec;

import com.aukocharlie.recorder4j.source.UniqueMethod;
import com.sun.jdi.Value;
import com.sun.source.tree.DoWhileLoopTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author auko
 */
public class DoWhileLoopStatementSpec implements ControlFlowStatement<Boolean> {

    BlockSpec loopBlock;
    Expression condition;

    public DoWhileLoopStatementSpec(DoWhileLoopTree node, CompilationUnitSpec compilationUnitSpec) {
        this.loopBlock = new BlockSpec(node.getStatement(), compilationUnitSpec);
        this.condition = new ExpressionSpec(node.getCondition(), compilationUnitSpec);
    }

    @Override
    public Boolean evaluateCondition(Map<UniqueMethod, Value> callResults) {
        return null;
    }


    @Override
    public List<BlockSpec> getLambdaBlockList() {
        List<BlockSpec> lambdaList = new ArrayList<>();
        loopBlock.statements.stream().map(Statement::getLambdaBlockList).forEach(lambdaList::addAll);
        lambdaList.addAll(condition.getLambdaBlockList());
        return lambdaList;
    }

    @Override
    public Expression nextExpression() {
        return null;
    }
}
