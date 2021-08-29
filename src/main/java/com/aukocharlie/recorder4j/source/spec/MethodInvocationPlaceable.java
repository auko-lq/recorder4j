package com.aukocharlie.recorder4j.source.spec;

import com.aukocharlie.recorder4j.source.spec.expression.MethodInvocationExpressionSpec;

/**
 * @author auko
 */
public interface MethodInvocationPlaceable {

    /**
     * TODO: delete this
     * Assume that <em>hasNextMethodInvocation</em> is a method that will be
     * called at any time, so we can't modify the properties of the object in
     * this method to prevent different results from different invocations.
     */
    boolean hasNextMethodInvocation();

    MethodInvocationExpressionSpec nextMethodInvocation();

}
