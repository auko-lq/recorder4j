package com.aukocharlie.recorder4j.source.spec.statement.loop;

import com.aukocharlie.recorder4j.source.MethodMetadata;
import com.aukocharlie.recorder4j.source.spec.AbstractMethodInvocationIterator;
import com.aukocharlie.recorder4j.source.spec.MethodInvocationPlaceableNode;
import com.aukocharlie.recorder4j.source.spec.block.AbstractBlockSpec;
import com.aukocharlie.recorder4j.source.spec.block.MethodBlockSpec;
import com.aukocharlie.recorder4j.source.spec.expression.Expression;
import com.aukocharlie.recorder4j.source.spec.expression.AbstractExpressionSpec;
import com.aukocharlie.recorder4j.source.spec.statement.ControlFlow;
import com.sun.jdi.Value;

import java.util.List;
import java.util.Map;

/**
 * @author auko
 */
public class LoopSpec extends AbstractMethodInvocationIterator implements ControlFlow<Boolean> {

    String labelName;

    AbstractBlockSpec loopBlock;

    AbstractExpressionSpec condition;

    MethodBlockSpec loopLocatedMethod;

    protected LoopSpec(){}

    @Override
    public Boolean evaluateCondition(Map<MethodMetadata, Value> callResults) {
        return null;
    }

    @Override
    public MethodInvocationPlaceableNode nextControlFlowNode() {
        return null;
    }

    @Override
    public Expression nextExpression() {
        return null;
    }

    @Override
    public List<AbstractBlockSpec> getLambdaBlockList() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void reset() {

    }

    @Override
    protected void setExecutionOrder() {

    }
}
