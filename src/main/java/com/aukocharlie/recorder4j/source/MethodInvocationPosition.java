package com.aukocharlie.recorder4j.source;

import java.util.ArrayList;
import java.util.List;

/**
 * @author auko
 * @date 2021/4/8 16:23
 */
public class MethodInvocationPosition extends SourcePosition {

    // example: test.one(another())
    private List<MethodInvocationPosition> methodInvocationPositionInArgs = new ArrayList<>();

    // For method chaining, example: test.one().another()
    private MethodInvocationPosition nextMethodInvocationInChain;

    public MethodInvocationPosition(SourcePosition sourcePosition) {
        super(sourcePosition.startPosition, sourcePosition.endPosition, sourcePosition.source);
    }

    public void addArgMethodPosition(MethodInvocationPosition position) {
        methodInvocationPositionInArgs.add(position);
    }

    public List<MethodInvocationPosition> getArgMethodPosition() {
        return this.methodInvocationPositionInArgs;
    }

    public void setNextMethod(MethodInvocationPosition nextMethod) {
        this.nextMethodInvocationInChain = nextMethod;
    }

    public MethodInvocationPosition nextMethod() {
        return this.nextMethodInvocationInChain;
    }
}
