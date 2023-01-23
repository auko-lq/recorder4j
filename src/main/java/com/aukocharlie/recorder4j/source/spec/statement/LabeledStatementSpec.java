package com.aukocharlie.recorder4j.source.spec.statement;

import com.aukocharlie.recorder4j.source.scanner.LabeledStatementScanner;
import com.aukocharlie.recorder4j.source.spec.*;
import com.aukocharlie.recorder4j.source.spec.block.AbstractBlockSpec;
import com.aukocharlie.recorder4j.source.spec.block.LoopBlockSpec;
import com.aukocharlie.recorder4j.source.spec.expression.Expression;
import com.aukocharlie.recorder4j.source.spec.expression.MethodInvocationExpressionSpec;
import com.sun.source.tree.*;

import java.util.List;

/**
 * @author auko
 */
public class LabeledStatementSpec extends AbstractStatementSpec implements Statement {

    LoopBlockSpec outerLoop;
    String labelName;

    Statement statement;

    public LabeledStatementSpec(LabeledStatementTree node, CompilationUnitSpec compilationUnitSpec, LoopBlockSpec outerLoop) {
        this.outerLoop = outerLoop;
        this.labelName = node.getLabel().toString();
        new LabeledStatementScanner(outerLoop, labelName).scan(node, compilationUnitSpec);
    }

    public LabeledStatementSpec(LabeledStatementTree node, CompilationUnitSpec compilationUnitSpec) {
        this(node, compilationUnitSpec, null);
    }

    @Override
    public Expression nextExpression() {
        return null;
    }

    @Override
    public List<AbstractBlockSpec> getLambdaBlockList() {
        return statement.getLambdaBlockList();
    }

    @Override
    protected void setExecutionOrder() {

    }

    @Override
    public boolean hasNextMethodInvocation() {
        return statement.hasNextMethodInvocation();
    }

    @Override
    public MethodInvocationExpressionSpec nextMethodInvocation() {
        return statement.nextMethodInvocation();
    }

    @Override
    public void reset() {

    }
}
