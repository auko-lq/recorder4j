package com.aukocharlie.recorder4j.source.spec.statement;

import com.aukocharlie.recorder4j.source.spec.block.BlockSpec;
import com.aukocharlie.recorder4j.source.spec.expression.Expression;
import com.aukocharlie.recorder4j.source.spec.expression.MethodInvocationExpressionSpec;
import com.sun.source.tree.Tree;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * For example:
 * <pre>
 *     int i;
 *     RuntimeException ex;
 * </pre>
 * <p>
 * Currently, this statement is only used for parameter of catch block in a try statement.
 *
 * @author auko
 */
public class UninitializedDeclarationStatementSpec implements Statement {

    Tree variableType;

    public UninitializedDeclarationStatementSpec(Tree type) {
        this.variableType = type;
    }

    @Override
    public Expression nextExpression() {
        return null;
    }

    @Override
    public List<BlockSpec> getLambdaBlockList() {
        return Collections.emptyList();
    }

    @Override
    public boolean hasNextMethodInvocation() {
        return false;
    }

    @Override
    public MethodInvocationExpressionSpec nextMethodInvocation() {
        throw new NoSuchElementException("There isn't next method invocation");
    }
}
