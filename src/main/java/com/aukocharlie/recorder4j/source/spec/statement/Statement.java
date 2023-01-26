package com.aukocharlie.recorder4j.source.spec.statement;

import com.aukocharlie.recorder4j.source.spec.LambdaPlaceable;
import com.aukocharlie.recorder4j.source.spec.MethodInvocationPlaceableNode;
import com.aukocharlie.recorder4j.source.spec.block.BlockStatement;
import com.aukocharlie.recorder4j.source.spec.expression.Expression;

/**
 * @author auko
 */
public interface Statement extends BlockStatement {

    Expression nextExpression();

}
