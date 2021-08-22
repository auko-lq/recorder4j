package com.aukocharlie.recorder4j.source.spec.statement;

import com.aukocharlie.recorder4j.source.spec.LambdaPlaceable;
import com.aukocharlie.recorder4j.source.spec.MethodInvocationPlaceable;
import com.aukocharlie.recorder4j.source.spec.expression.Expression;

/**
 * @author auko
 */
public interface Statement extends LambdaPlaceable, MethodInvocationPlaceable {

    Expression nextExpression();

}
