package com.aukocharlie.recorder4j.source;

import java.util.ArrayList;
import java.util.List;

/**
 * @author auko
 * @date 2021/4/8 16:23
 */
public class MethodInvocationPosition extends SourcePosition {

    // example: test.one(another())
    private List<MethodInvocationPosition> methodInvocationPositionInParameters = new ArrayList<>();

    // For method chaining
    private MethodInvocationPosition nextMethodInvocationInChain;

    public MethodInvocationPosition(SourcePosition sourcePosition) {
        super(sourcePosition.startPosition, sourcePosition.endPosition, sourcePosition.source);
    }

    public void addParameterMethodPosition(MethodInvocationPosition position) {
        methodInvocationPositionInParameters.add(position);
    }

    public List<MethodInvocationPosition> getParamMethodPosition() {
        return this.methodInvocationPositionInParameters;
    }

    public void setNextMethod(MethodInvocationPosition nextMethod) {
        this.nextMethodInvocationInChain = nextMethod;
    }

    public MethodInvocationPosition nextMethod() {
        return this.nextMethodInvocationInChain;
    }
}
