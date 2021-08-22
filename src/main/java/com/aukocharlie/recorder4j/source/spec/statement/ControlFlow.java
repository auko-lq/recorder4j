package com.aukocharlie.recorder4j.source.spec.statement;

import com.aukocharlie.recorder4j.source.UniqueMethod;
import com.aukocharlie.recorder4j.source.spec.expression.Expression;
import com.aukocharlie.recorder4j.source.spec.expression.MethodInvocationExpressionSpec;
import com.aukocharlie.recorder4j.source.spec.block.BlockSpec;
import com.sun.jdi.Value;

import java.util.List;
import java.util.Map;

/**
 * @author auko
 */
public interface ControlFlow<C> extends Statement {

    C evaluateCondition(Map<UniqueMethod, Value> callResults);

}

class ControlFlowStatementSpec implements Statement{

    @Override
    public Expression nextExpression() {
        return null;
    }

    @Override
    public List<BlockSpec> getLambdaBlockList() {
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
}