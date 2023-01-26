package com.aukocharlie.recorder4j.source.scanner;

import com.aukocharlie.recorder4j.source.spec.CompilationUnitSpec;
import com.aukocharlie.recorder4j.source.spec.block.LoopBlockSpec;
import com.aukocharlie.recorder4j.source.spec.statement.BreakStatementSpec;
import com.aukocharlie.recorder4j.source.spec.statement.ContinueStatementSpec;
import com.aukocharlie.recorder4j.source.spec.statement.LabeledStatementSpec;
import com.aukocharlie.recorder4j.source.spec.statement.loop.DoWhileLoopSpec;
import com.aukocharlie.recorder4j.source.spec.statement.loop.ForLoopSpec;
import com.aukocharlie.recorder4j.source.spec.statement.loop.WhileLoopSpec;
import com.sun.source.tree.*;

public class LoopBlockScanner extends BlockScanner {

    public LoopBlockScanner(LoopBlockSpec statementLocatedBlock) {
        super(statementLocatedBlock);
    }

    @Override
    public Void visitDoWhileLoop(DoWhileLoopTree node, CompilationUnitSpec compilationUnitSpec) {
        blockStatements.add(new DoWhileLoopSpec(node, compilationUnitSpec, (LoopBlockSpec) statementLocatedBlock, null));
        return null;
    }

    @Override
    public Void visitWhileLoop(WhileLoopTree node, CompilationUnitSpec compilationUnitSpec) {
        blockStatements.add(new WhileLoopSpec(node, compilationUnitSpec, (LoopBlockSpec) statementLocatedBlock, null));
        return null;
    }

    @Override
    public Void visitForLoop(ForLoopTree node, CompilationUnitSpec compilationUnitSpec) {
        blockStatements.add(new ForLoopSpec(node, compilationUnitSpec, (LoopBlockSpec) statementLocatedBlock, null));
        return null;
    }

    @Override
    public Void visitLabeledStatement(LabeledStatementTree node, CompilationUnitSpec compilationUnitSpec) {
        blockStatements.add(new LabeledStatementSpec(node, compilationUnitSpec, (LoopBlockSpec) statementLocatedBlock));
        return null;
    }

    @Override
    public Void visitBreak(BreakTree node, CompilationUnitSpec compilationUnitSpec) {
        blockStatements.add(new BreakStatementSpec(node, (LoopBlockSpec) statementLocatedBlock));
        return null;
    }

    @Override
    public Void visitContinue(ContinueTree node, CompilationUnitSpec compilationUnitSpec) {
        blockStatements.add(new ContinueStatementSpec(node, (LoopBlockSpec) statementLocatedBlock));
        return null;
    }
}