package com.aukocharlie.recorder4j.source.spec;

/**
 * @author auko
 */
public interface Statement extends LambdaPlaceable, MethodInvocationPlaceable {

    Expression nextExpression();

}
