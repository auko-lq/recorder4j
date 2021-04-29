package com.aukocharlie.recorder4j.source.spec;


import com.aukocharlie.recorder4j.source.MethodInvocationPosition;

import java.util.List;

/**
 * @author auko
 */
public interface Expression extends LambdaAllowed {

    List<MethodInvocationPosition> getMethodInvocations();

}
