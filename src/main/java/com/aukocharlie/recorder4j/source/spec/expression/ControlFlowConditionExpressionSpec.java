package com.aukocharlie.recorder4j.source.spec.expression;

import com.aukocharlie.recorder4j.source.spec.block.AbstractBlockSpec;

import java.util.List;

/**
 * @author auko
 */
public class ControlFlowConditionExpressionSpec extends AbstractExpressionSpec{


    public ControlFlowConditionExpressionSpec(String expr) {
        super(expr);
    }

    @Override
    protected void setExecutionOrder() {

    }

    @Override
    public List<AbstractBlockSpec> getLambdaBlockList() {
        return null;
    }

    @Override
    public void reset() {

    }
}
