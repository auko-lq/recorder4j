package com.aukocharlie.recorder4j.source.spec;

import com.aukocharlie.recorder4j.source.spec.expression.MethodInvocationExpressionSpec;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author auko
 */
public interface MethodInvocationPlaceableNode {

    /**
     * Check whether node has next invocation in order.
     * <p>
     * Pay attention to idempotence.
     */
    boolean hasNextMethodInvocation();

    /**
     * Find the next method invocation in order from all nodes
     *
     * @throws java.util.NoSuchElementException No next method invocation
     */
    MethodInvocationExpressionSpec nextMethodInvocation();

    void reset();

}
