package com.aukocharlie.recorder4j.source.spec.statement.loop;

import com.aukocharlie.recorder4j.source.MethodMetadata;
import com.aukocharlie.recorder4j.source.spec.*;
import com.aukocharlie.recorder4j.source.spec.block.AbstractBlockSpec;
import com.aukocharlie.recorder4j.source.spec.block.LoopBlockSpec;
import com.aukocharlie.recorder4j.source.spec.expression.Expression;
import com.aukocharlie.recorder4j.source.spec.expression.AbstractExpressionSpec;
import com.sun.jdi.Value;
import com.sun.source.tree.DoWhileLoopTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author auko
 */
public class DoWhileLoopSpec extends LoopSpec {

    public DoWhileLoopSpec(DoWhileLoopTree node, CompilationUnitSpec compilationUnitSpec) {
        this(node, compilationUnitSpec, null, null);
    }

    public DoWhileLoopSpec(DoWhileLoopTree node, CompilationUnitSpec compilationUnitSpec, LoopBlockSpec outerLoop, String labelName) {
        this.labelName = labelName;
        this.condition = AbstractExpressionSpec.toSpecificExpression(node.getCondition(), compilationUnitSpec);
        this.loopBlock = new LoopBlockSpec(node.getStatement(), compilationUnitSpec, outerLoop, labelName);
    }

    @Override
    public Boolean evaluateCondition(Map<MethodMetadata, Value> callResults) {
        return null;
    }

    @Override
    public Expression nextExpression() {
        return null;
    }

    @Override
    public List<AbstractBlockSpec> getLambdaBlockList() {
        List<AbstractBlockSpec> lambdaList = new ArrayList<>();
        lambdaList.addAll(loopBlock.getLambdaBlockList());
        lambdaList.addAll(condition.getLambdaBlockList());
        return lambdaList;
    }

    @Override
    public boolean hasNextMethodInvocation() {
        if (loopBlock.hasNextMethodInvocation()) {
            return true;
        }
        loopBlock.reset();

        return condition.hasNextMethodInvocation();
    }

}
