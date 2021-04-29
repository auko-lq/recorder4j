package com.aukocharlie.recorder4j.source.spec;

import com.aukocharlie.recorder4j.source.UniqueMethod;
import com.sun.jdi.Value;
import com.sun.source.tree.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author auko
 */
public class ForLoopStatementSpec implements ControlFlowStatement<Boolean> {

    List<Statement> initializers = new ArrayList<>();
    Expression condition;
    BlockSpec loopBlock;
    List<Expression> updates;

    public ForLoopStatementSpec(ForLoopTree node, CompilationUnitSpec compilationUnitSpec) {
        node.getInitializer().forEach((initializer) -> {
            if (initializer instanceof VariableTree) {
                initializers.add(new InitializerOrAssignmentSpec((VariableTree) initializer, compilationUnitSpec));
            } else if (initializer instanceof ExpressionStatementTree) {
                ExpressionTree expressionTree = ((ExpressionStatementTree) initializer).getExpression();
                if (expressionTree instanceof AssignmentTree) {
                    initializers.add(new InitializerOrAssignmentSpec((AssignmentTree) expressionTree, compilationUnitSpec));
                }
            }
        });

        this.condition = new ExpressionSpec(node.getCondition(), compilationUnitSpec);
        node.getUpdate().forEach((update) ->
                updates.add(new ExpressionSpec(update.getExpression(), compilationUnitSpec)));
        this.loopBlock = new BlockSpec(node.getStatement(), compilationUnitSpec);
    }

    @Override
    public Boolean evaluateCondition(Map<UniqueMethod, Value> callResults) {
        return null;
    }


    @Override
    public List<BlockSpec> getLambdaBlockList() {
        List<BlockSpec> lambdaList = new ArrayList<>();
        initializers.stream().map(Statement::getLambdaBlockList).forEach(lambdaList::addAll);
        lambdaList.addAll(condition.getLambdaBlockList());
        updates.stream().map(LambdaAllowed::getLambdaBlockList).forEach(lambdaList::addAll);
        lambdaList.addAll(loopBlock.getLambdaBlockList());
        return lambdaList;
    }

    @Override
    public Expression nextExpression() {
        return null;
    }
}
