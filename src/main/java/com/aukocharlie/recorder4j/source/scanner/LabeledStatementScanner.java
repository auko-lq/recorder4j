package com.aukocharlie.recorder4j.source.scanner;

import com.aukocharlie.recorder4j.source.SourceScanner;
import com.aukocharlie.recorder4j.source.spec.CompilationUnitSpec;
import com.aukocharlie.recorder4j.source.spec.block.LoopBlockSpec;
import com.aukocharlie.recorder4j.source.spec.statement.Statement;
import com.aukocharlie.recorder4j.source.spec.statement.loop.DoWhileLoopSpec;
import com.aukocharlie.recorder4j.source.spec.statement.loop.ForLoopSpec;
import com.aukocharlie.recorder4j.source.spec.statement.loop.WhileLoopSpec;
import com.sun.source.tree.DoWhileLoopTree;
import com.sun.source.tree.ForLoopTree;
import com.sun.source.tree.WhileLoopTree;
import lombok.Getter;

public class LabeledStatementScanner extends SourceScanner {

    @Getter
    private Statement statement;
    private final LoopBlockSpec outerLoop;

    private final String labelName;

    public LabeledStatementScanner(LoopBlockSpec outerLoop, String labelName) {
        this.outerLoop = outerLoop;
        this.labelName = labelName;
    }

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