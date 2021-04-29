package com.aukocharlie.recorder4j.source.spec;

import com.aukocharlie.recorder4j.source.UniqueMethod;
import com.sun.jdi.Value;
import com.sun.source.tree.WhileLoopTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author auko
 */
public class WhileLoopStatementSpec implements ControlFlowStatement<Boolean> {

    Expression condition;
    BlockSpec loopBlock;

    public WhileLoopStatementSpec(WhileLoopTree node, CompilationUnitSpec compilationUnitSpec) {
        this.condition = new ExpressionSpec(node.getCondition(), compilationUnitSpec);
        this.loopBlock = new BlockSpec(node.getStatement(), compilationUnitSpec);
    }

    @Override
    public Boolean evaluateCondition(Map<UniqueMethod, Value> callResults) {
        return null;
    }


    @Override
    public List<BlockSpec> getLambdaBlockList() {
        List<BlockSpec> lambdaList = new ArrayList<>();
        lambdaList.addAll(condition.getLambdaBlockList());
        loopBlock.statements.stream().map(Statement::getLambdaBlockList).forEach(lambdaList::addAll);
        return lambdaList;
    }

    @Override
    public Expression nextExpression() {
        return null;
    }
}
