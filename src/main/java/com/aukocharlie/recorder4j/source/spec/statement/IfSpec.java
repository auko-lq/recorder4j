package com.aukocharlie.recorder4j.source.spec.statement;

import com.aukocharlie.recorder4j.source.UniqueMethod;
import com.aukocharlie.recorder4j.source.spec.CompilationUnitSpec;
import com.aukocharlie.recorder4j.source.spec.expression.Expression;
import com.aukocharlie.recorder4j.source.spec.expression.ExpressionSpec;
import com.aukocharlie.recorder4j.source.spec.block.BlockSpec;
import com.aukocharlie.recorder4j.source.spec.expression.MethodInvocationExpressionSpec;
import com.sun.jdi.Value;
import com.sun.source.tree.IfTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author auko
 */
public class IfSpec implements ControlFlow<Boolean> {

    ExpressionSpec condition;
    BlockSpec thenBlock;
    BlockSpec elseBlock;

    public IfSpec(IfTree node, CompilationUnitSpec compilationUnitSpec) {
        this.condition = ExpressionSpec.toSpecificExpression(node.getCondition(), compilationUnitSpec);
        this.thenBlock = new BlockSpec(node.getThenStatement(), compilationUnitSpec);
        this.elseBlock = new BlockSpec(node.getElseStatement(), compilationUnitSpec);

        a:
        while(true){
            if(true){
                break a;
            }
            try{
                break a;
            }finally {
                break a;
            }
        }
    }

    @Override
    public Boolean evaluateCondition(Map<UniqueMethod, Value> callResults) {
        return null;
    }

    @Override
    public Expression nextExpression() {
        return null;
    }

    @Override
    public List<BlockSpec> getLambdaBlockList() {
        List<BlockSpec> lambdaList = new ArrayList<>();
        lambdaList.addAll(condition.getLambdaBlockList());
        lambdaList.addAll(thenBlock.getLambdaBlockList());
        lambdaList.addAll(elseBlock.getLambdaBlockList());
        return lambdaList;
    }

    @Override
    public boolean hasNextMethodInvocation() {
        return false;
    }

    @Override
    public MethodInvocationExpressionSpec nextMethodInvocation() {
        return null;
    }
}
