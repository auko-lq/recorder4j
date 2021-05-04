package com.aukocharlie.recorder4j.source.spec;

import com.aukocharlie.recorder4j.source.UniqueMethod;
import com.sun.jdi.Value;

import java.util.List;
import java.util.Map;

/**
 * @author auko
 */
public interface ControlFlow<C> extends Statement {

    C evaluateCondition(Map<UniqueMethod, Value> callResults);

}

class ControlFlowStatementSpec implements Statement{

    @Override
    public Expression nextExpression() {
        return null;
    }

    @Override
    public List<BlockSpec> getLambdaBlockList() {
        temp:
        System.out.println("");
        outer:
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                continue outer;
            }
            inner1:
            do{

            }while ("".length() == 0);

            inner1:
            do{
                break inner1;
            }while (true);
        }
        return null;
    }

    @Override
    public boolean hasNextMethodInvocation() {
        return false;
    }

    @Override
    public MethodInvocationPosition nextMethodInvocation() {
        return null;
    }
}