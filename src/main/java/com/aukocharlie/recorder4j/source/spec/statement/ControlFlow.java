package com.aukocharlie.recorder4j.source.spec.statement;

import com.aukocharlie.recorder4j.source.MethodMetadata;
import com.aukocharlie.recorder4j.source.spec.MethodInvocationPlaceableNode;
import com.sun.jdi.Value;

import java.util.Map;

/**
 * @author auko
 */
public interface ControlFlow<C> extends Statement {

    C evaluateCondition(Map<MethodMetadata, Value> callResults);

    MethodInvocationPlaceableNode nextControlFlowNode();

}
