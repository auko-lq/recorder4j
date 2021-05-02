package com.aukocharlie.recorder4j.source.spec;

import com.aukocharlie.recorder4j.source.UniqueMethod;
import com.sun.jdi.Value;

import java.util.Map;

/**
 * @author auko
 */
public interface ControlFlow<C> extends Statement {

    C evaluateCondition(Map<UniqueMethod, Value> callResults);

}
