package com.aukocharlie.recorder4j.source.spec.statement.loop;

import com.aukocharlie.recorder4j.source.UniqueMethod;
import com.aukocharlie.recorder4j.source.spec.*;
import com.aukocharlie.recorder4j.source.spec.block.BlockSpec;
import com.aukocharlie.recorder4j.source.spec.block.LoopBlockSpec;
import com.aukocharlie.recorder4j.source.spec.block.MethodBlockSpec;
import com.aukocharlie.recorder4j.source.spec.expression.Expression;
import com.aukocharlie.recorder4j.source.spec.expression.ExpressionSpec;
import com.aukocharlie.recorder4j.source.spec.expression.MethodInvocationExpressionSpec;
import com.aukocharlie.recorder4j.source.spec.statement.ControlFlow;
import com.sun.jdi.Value;
import com.sun.source.tree.DoWhileLoopTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author auko
 */
public class DoWhileLoopSpec implements ControlFlow<Boolean> {

    String labelName;

    BlockSpec loopBlock;
    ExpressionSpec condition;
    MethodBlockSpec loopLocatedMethod;

    public DoWhileLoopSpec(DoWhileLoopTree node, CompilationUnitSpec compilationUnitSpec) {
        this(node, compilationUnitSpec, null, null);
    }

    public DoWhileLoopSpec(DoWhileLoopTree node, CompilationUnitSpec compilationUnitSpec, LoopBlockSpec outerLoop, String labelName) {
        this.labelName = labelName;
        this.condition = ExpressionSpec.toSpecificExpression(node.getCondition(), compilationUnitSpec);
        this.loopBlock = new LoopBlockSpec(node.getStatement(), compilationUnitSpec, outerLoop, labelName);
    }

    public DoWhileLoopSpec(DoWhileLoopTree node, CompilationUnitSpec compilationUnitSpec, MethodBlockSpec locatedMethodBlock) {
        this.loopLocatedMethod = locatedMethodBlock;
        this.condition = ExpressionSpec.toSpecificExpression(node.getCondition(), compilationUnitSpec);
        this.loopBlock = new LoopBlockSpec(node.getStatement(), compilationUnitSpec, null, null);
    }

    @Override
    public Boolean evaluateCondition(Map<UniqueMethod, Value> callResults) {
        return null;
    }


    @Override
    public List<BlockSpec> getLambdaBlockList() {
        List<BlockSpec> lambdaList = new ArrayList<>();
        lambdaList.addAll(loopBlock.getLambdaBlockList());
        lambdaList.addAll(condition.getLambdaBlockList());
        return lambdaList;
    }

    @Override
    public Expression nextExpression() {
        return null;
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
