package com.aukocharlie.recorder4j.source.spec.statement.loop;

import com.aukocharlie.recorder4j.source.MethodMetadata;
import com.aukocharlie.recorder4j.source.spec.*;
import com.aukocharlie.recorder4j.source.spec.block.AbstractBlockSpec;
import com.aukocharlie.recorder4j.source.spec.block.LoopBlockSpec;
import com.aukocharlie.recorder4j.source.spec.expression.Expression;
import com.aukocharlie.recorder4j.source.spec.expression.AbstractExpressionSpec;
import com.aukocharlie.recorder4j.source.spec.expression.MethodInvocationExpressionSpec;
import com.aukocharlie.recorder4j.source.spec.statement.InitializerOrAssignmentSpec;
import com.aukocharlie.recorder4j.source.spec.statement.Statement;
import com.sun.jdi.Value;
import com.sun.source.tree.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author auko
 */
public class ForLoopSpec extends LoopSpec {

    List<Statement> initializers = new ArrayList<>();
    List<AbstractExpressionSpec> updates = new ArrayList<>();

    public ForLoopSpec(ForLoopTree node, CompilationUnitSpec compilationUnitSpec, LoopBlockSpec outerLoop, String labelName) {
        this.labelName = labelName;
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

        this.condition = AbstractExpressionSpec.toSpecificExpression(node.getCondition(), compilationUnitSpec);
        node.getUpdate().forEach((update) -> updates.add(AbstractExpressionSpec.toSpecificExpression(update.getExpression(), compilationUnitSpec))
        );
        this.loopBlock = new LoopBlockSpec(node.getStatement(), compilationUnitSpec, outerLoop, labelName);
    }

    public ForLoopSpec(ForLoopTree node, CompilationUnitSpec compilationUnitSpec) {
        this(node, compilationUnitSpec, null, null);
    }

    @Override
    public Boolean evaluateCondition(Map<MethodMetadata, Value> callResults) {
        return null;
    }


    @Override
    public List<AbstractBlockSpec> getLambdaBlockList() {
        List<AbstractBlockSpec> lambdaList = new ArrayList<>();
        initializers.stream().map(Statement::getLambdaBlockList).forEach(lambdaList::addAll);
        lambdaList.addAll(condition.getLambdaBlockList());
        updates.stream().map(LambdaPlaceable::getLambdaBlockList).forEach(lambdaList::addAll);
        lambdaList.addAll(loopBlock.getLambdaBlockList());
        return lambdaList;
    }

    @Override
    public Expression nextExpression() {
        return null;
    }

    @Override
    public boolean hasNextMethodInvocation() {
        for (Statement initializerStatement : initializers) {
            if (initializerStatement.hasNextMethodInvocation()) {
                return true;
            }
        }

        if (condition.hasNextMethodInvocation()) {
            return true;
        }

        if (loopBlock.hasNextMethodInvocation()) {
            return true;
        }
        loopBlock.reset();

        for (AbstractExpressionSpec updateExpression : updates) {
            if (updateExpression.hasNextMethodInvocation()) {
                return true;
            }
        }

        return false;
    }

    @Override
    public MethodInvocationExpressionSpec nextMethodInvocation() {
        return null;
    }
}
