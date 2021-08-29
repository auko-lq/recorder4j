package com.aukocharlie.recorder4j.source.spec.statement.loop;

import com.aukocharlie.recorder4j.source.UniqueMethod;
import com.aukocharlie.recorder4j.source.spec.block.BlockSpec;
import com.aukocharlie.recorder4j.source.spec.block.MethodBlockSpec;
import com.aukocharlie.recorder4j.source.spec.expression.Expression;
import com.aukocharlie.recorder4j.source.spec.expression.ExpressionSpec;
import com.aukocharlie.recorder4j.source.spec.expression.MethodInvocationExpressionSpec;
import com.aukocharlie.recorder4j.source.spec.statement.ControlFlow;
import com.sun.jdi.Value;

import java.util.List;
import java.util.Map;

/**
 * @author auko
 */
public class LoopSpec implements ControlFlow<Boolean> {

    String labelName;

    BlockSpec loopBlock;

    ExpressionSpec condition;

    MethodBlockSpec loopLocatedMethod;

    protected LoopSpec(){}

    @Override
    public Boolean evaluateCondition(Map<UniqueMethod, Value> callResults) {
        return null;
    }

    @Override
    public Expression nextExpression() {
        return null;
    }

    @Override
    public List<BlockSpec> getLambdaBlockList() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasNextMethodInvocation() {
        throw new UnsupportedOperationException();
    }

    @Override
    public MethodInvocationExpressionSpec nextMethodInvocation() {
        throw new UnsupportedOperationException();
    }
}
