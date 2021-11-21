package com.aukocharlie.recorder4j.source.spec;

import com.aukocharlie.recorder4j.source.spec.expression.MethodInvocationExpressionSpec;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author auko
 */
public abstract class AbstractMethodInvocationIterator implements MethodInvocationPlaceableNode {

    public List<MethodInvocationPlaceableNode> nodeInExecutionOrder = new ArrayList<>();

    private int currentNodeIndex = 0;

    protected abstract void setExecutionOrder();

    /**
     * Check whether nodes have next invocation in order.
     * <p>
     * Pay attention to idempotence.
     */
    public boolean hasNextMethodInvocation() {
        for (int i = currentNodeIndex; i < nodeInExecutionOrder.size(); i++) {
            if (nodeInExecutionOrder.get(i).hasNextMethodInvocation()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Find the next method invocation in order from all nodes
     *
     * @throws java.util.NoSuchElementException No next method invocation
     */
    public MethodInvocationExpressionSpec nextMethodInvocation() {
        for (; currentNodeIndex < nodeInExecutionOrder.size(); currentNodeIndex++) {
            if (nodeInExecutionOrder.get(currentNodeIndex).hasNextMethodInvocation()) {
                return nodeInExecutionOrder.get(currentNodeIndex).nextMethodInvocation();
            } else {
                nodeInExecutionOrder.get(currentNodeIndex).reset();
            }
        }
        throw new NoSuchElementException("There isn't next method invocation");
    }

}
