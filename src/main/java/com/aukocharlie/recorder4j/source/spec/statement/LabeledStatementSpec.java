package com.aukocharlie.recorder4j.source.spec.statement;

import com.aukocharlie.recorder4j.source.SourceScanner;
import com.aukocharlie.recorder4j.source.spec.*;
import com.aukocharlie.recorder4j.source.spec.block.BlockSpec;
import com.aukocharlie.recorder4j.source.spec.block.LoopBlockSpec;
import com.aukocharlie.recorder4j.source.spec.expression.Expression;
import com.aukocharlie.recorder4j.source.spec.expression.MethodInvocationExpressionSpec;
import com.aukocharlie.recorder4j.source.spec.statement.loop.DoWhileLoopSpec;
import com.aukocharlie.recorder4j.source.spec.statement.loop.ForLoopSpec;
import com.aukocharlie.recorder4j.source.spec.statement.loop.WhileLoopSpec;
import com.sun.source.tree.*;

import java.util.List;

/**
 * @author auko
 */
public class LabeledStatementSpec implements Statement {

    LoopBlockSpec outerLoop;
    String labelName;

    Statement statement;

    public LabeledStatementSpec(LabeledStatementTree node, CompilationUnitSpec compilationUnitSpec, LoopBlockSpec outerLoop) {
        this.outerLoop = outerLoop;
        this.labelName = node.getLabel().toString();
        new LabeledStatementScanner().scan(node, compilationUnitSpec);
    }

    public LabeledStatementSpec(LabeledStatementTree node, CompilationUnitSpec compilationUnitSpec) {
        this(node, compilationUnitSpec, null);
    }

    class LabeledStatementScanner extends SourceScanner {

        @Override
        public Void visitDoWhileLoop(DoWhileLoopTree node, CompilationUnitSpec compilationUnitSpec) {
            statement = new DoWhileLoopSpec(node, compilationUnitSpec, outerLoop, labelName);
            return null;
        }

        @Override
        public Void visitWhileLoop(WhileLoopTree node, CompilationUnitSpec compilationUnitSpec) {
            statement = new WhileLoopSpec(node, compilationUnitSpec, outerLoop, labelName);
            return null;
        }

        @Override
        public Void visitForLoop(ForLoopTree node, CompilationUnitSpec compilationUnitSpec) {
            statement = new ForLoopSpec(node, compilationUnitSpec, outerLoop, labelName);
            return null;
        }

    }

    @Override
    public Expression nextExpression() {
        return null;
    }

    @Override
    public List<BlockSpec> getLambdaBlockList() {
        return statement.getLambdaBlockList();
    }

    @Override
    public boolean hasNextMethodInvocation() {
        return statement.hasNextMethodInvocation();
    }

    @Override
    public MethodInvocationExpressionSpec nextMethodInvocation() {
        return statement.nextMethodInvocation();
    }
}
