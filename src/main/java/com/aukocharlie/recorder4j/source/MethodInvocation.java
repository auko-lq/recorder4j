package com.aukocharlie.recorder4j.source;

import com.aukocharlie.recorder4j.source.spec.MethodInvocationPosition;
import com.sun.jdi.Value;
import lombok.Data;

/**
 * @author auko
 */
@Data
public class MethodInvocation {

    private MethodMetadata method;

    private MethodInvocationPosition position;

    private Value invocationResult;

}
