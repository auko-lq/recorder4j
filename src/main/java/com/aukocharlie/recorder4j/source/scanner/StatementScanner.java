package com.aukocharlie.recorder4j.source.scanner;

import com.aukocharlie.recorder4j.source.SourceScanner;
import com.aukocharlie.recorder4j.source.spec.CompilationUnitSpec;
import com.aukocharlie.recorder4j.source.spec.block.AbstractBlockSpec;
import com.aukocharlie.recorder4j.source.spec.block.LoopBlockSpec;
import com.aukocharlie.recorder4j.source.spec.block.MethodBlockSpec;
import com.aukocharlie.recorder4j.source.spec.statement.*;
import com.aukocharlie.recorder4j.source.spec.statement.loop.DoWhileLoopSpec;
import com.aukocharlie.recorder4j.source.spec.statement.loop.ForLoopSpec;
import com.aukocharlie.recorder4j.source.spec.statement.loop.WhileLoopSpec;
import com.aukocharlie.recorder4j.util.Assert;
import com.sun.source.tree.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class StatementScanner extends SourceScanner {

    protected AbstractBlockSpec statementLocatedBlock;

    @Getter
    protected final List<Statement> statements = new ArrayList<>();

    public StatementScanner() {
    }

    public StatementScanner(AbstractBlockSpec statementLocatedBlock) {
        this.statementLocatedBlock = statementLocatedBlock;
    }

    @Override
    public Void visitVariable(VariableTree node, CompilationUnitSpec compilationUnitSpec) {
        statements.add(new InitializerOrAssignmentSpec(node, compilationUnitSpec));
        return null;
    }

    @Override
    public Void visitExpressionStatement(ExpressionStatementTree node, CompilationUnitSpec compilationUnitSpec) {
        statements.add(new ExpressionStatementSpec(node, compilationUnitSpec));
        return null;
    }

    @Override
    public Void visitDoWhileLoop(DoWhileLoopTree node, CompilationUnitSpec compilationUnitSpec) {
        statements.add(new DoWhileLoopSpec(node, compilationUnitSpec));
        return null;
    }

    @Override
    public Void visitWhileLoop(WhileLoopTree node, CompilationUnitSpec compilationUnitSpec) {
        statements.add(new WhileLoopSpec(node, compilationUnitSpec));
        return null;
    }

    @Override
    public Void visitForLoop(ForLoopTree node, CompilationUnitSpec compilationUnitSpec) {
        statements.add(new ForLoopSpec(node, compilationUnitSpec));
        return null;
    }

    @Override
    public Void visitIf(IfTree node, CompilationUnitSpec compilationUnitSpec) {
        statements.add(new IfSpec(node, compilationUnitSpec));
        return null;
    }

    @Override
    public Void visitTry(TryTree node, CompilationUnitSpec compilationUnitSpec) {
        statements.add(new TryCatchFinallyStatementSpec(node, compilationUnitSpec));
        return null;
    }

    @Override
    public Void visitLabeledStatement(LabeledStatementTree node, CompilationUnitSpec compilationUnitSpec) {
        statements.add(new LabeledStatementSpec(node, compilationUnitSpec));
        return null;
    }

    /**
     * Only {@link MethodBlockSpec} and {@link LoopBlockSpec} will have {@link ReturnStatementSpec}
     */
    @Override
    public Void visitReturn(ReturnTree node, CompilationUnitSpec compilationUnitSpec) {
        Assert.assertTrue(statementLocatedBlock instanceof MethodBlockSpec
                        || statementLocatedBlock instanceof LoopBlockSpec,
                "Unsupported statementLocatedBlock class: " + statementLocatedBlock.getClass());
        statements.add(new ReturnStatementSpec(node, compilationUnitSpec, statementLocatedBlock));
        return null;
    }
}