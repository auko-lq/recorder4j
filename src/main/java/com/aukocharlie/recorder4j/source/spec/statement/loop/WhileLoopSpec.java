package com.aukocharlie.recorder4j.source.spec.statement.loop;

import com.aukocharlie.recorder4j.source.UniqueMethod;
import com.aukocharlie.recorder4j.source.spec.*;
import com.aukocharlie.recorder4j.source.spec.block.BlockSpec;
import com.aukocharlie.recorder4j.source.spec.block.LoopBlockSpec;
import com.aukocharlie.recorder4j.source.spec.expression.Expression;
import com.aukocharlie.recorder4j.source.spec.expression.ExpressionSpec;
import com.aukocharlie.recorder4j.source.spec.expression.MethodInvocationExpressionSpec;
import com.aukocharlie.recorder4j.source.spec.statement.ControlFlow;
import com.sun.jdi.Value;
import com.sun.source.tree.WhileLoopTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author auko
 */
public class WhileLoopSpec extends LoopSpec {

    public WhileLoopSpec(WhileLoopTree node, CompilationUnitSpec compilationUnitSpec, LoopBlockSpec outerLoop, String labelName) {
        this.condition = ExpressionSpec.toSpecificExpression(node.getCondition(), compilationUnitSpec);
        this.loopBlock = new LoopBlockSpec(node.getStatement(), compilationUnitSpec, outerLoop, labelName);
        this.labelName = labelName;
    }

    public WhileLoopSpec(WhileLoopTree node, CompilationUnitSpec compilationUnitSpec) {
        this(node, compilationUnitSpec, null, null);
    }

    @Override
    public Boolean evaluateCondition(Map<UniqueMethod, Value> callResults) {
        return null;
    }


    @Override
    public List<BlockSpec> getLambdaBlockList() {
        List<BlockSpec> lambdaList = new ArrayList<>();
        lambdaList.addAll(condition.getLambdaBlockList());
        lambdaList.addAll(loopBlock.getLambdaBlockList());
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
