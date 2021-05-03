package com.aukocharlie.recorder4j.source.spec;

/**
 * @author auko
 */
public interface MethodInvocationPlaceable {

    boolean hasNextMethodInvocation();

    MethodInvocationPosition nextMethodInvocation();

}
