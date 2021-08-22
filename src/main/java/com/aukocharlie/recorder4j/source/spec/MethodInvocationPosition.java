package com.aukocharlie.recorder4j.source.spec;

import com.aukocharlie.recorder4j.source.SourcePosition;

public class MethodInvocationPosition extends SourcePosition {

    public MethodInvocationPosition(SourcePosition sourcePosition) {
        super(sourcePosition.startPosition, sourcePosition.endPosition, sourcePosition.source);
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", this.source, super.toString());
    }

}
