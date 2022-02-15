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

    private boolean setOrder = false;

    /**
     * Check whether nodes have next invocation in order.
     * <p>
     * Pay attention to idempotence.
     */
    public boolean hasNextMethodInvocation() {
        if (!setOrder) {
            setExecutionOrder();
            setOrder = true;
        }
        for (int i = currentNodeIndex; i < nodeInExecutionOrder.size(); i++) {
            MethodInvocationPlaceableNode node = nodeInExecutionOrder.get(i);
            if (node == this && node instanceof MethodInvocationExpressionSpec) {
                if (!((MethodInvocationExpressionSpec) node).isScanned()) {
                    return true;
                }
            } else if (node.hasNextMethodInvocation()) {
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
            MethodInvocationPlaceableNode node = nodeInExecutionOrder.get(currentNodeIndex);
            if (node == this && node instanceof MethodInvocationExpressionSpec) {
                MethodInvocationExpressionSpec castedNode = (MethodInvocationExpressionSpec) node;
                if (!castedNode.isScanned()) {
                    castedNode.setScanned(true);
                    return castedNode;
                }
            } else if (node.hasNextMethodInvocation()) {
                return node.nextMethodInvocation();
            } else {
                node.reset();
            }
        }
        throw new NoSuchElementException("There isn't next method invocation");
    }

}
