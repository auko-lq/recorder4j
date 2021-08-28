package com.aukocharlie.recorder4j.source.spec.block;

import com.aukocharlie.recorder4j.source.spec.CompilationUnitSpec;
import com.aukocharlie.recorder4j.source.spec.statement.BreakStatementSpec;
import com.aukocharlie.recorder4j.source.spec.statement.ContinueStatementSpec;
import com.aukocharlie.recorder4j.source.spec.statement.LabeledStatementSpec;
import com.aukocharlie.recorder4j.source.spec.statement.loop.DoWhileLoopSpec;
import com.aukocharlie.recorder4j.source.spec.statement.loop.ForLoopSpec;
import com.aukocharlie.recorder4j.source.spec.statement.loop.WhileLoopSpec;
import com.sun.source.tree.*;

/**
 * @author auko
 */
public class LoopBlockSpec extends BlockSpec {

    BlockSpec outerBlock;
    String labelName;

    /**
     * Mark whether the break statement is executed in the loop block.
     */
    boolean broken = false;

    public LoopBlockSpec(StatementTree node, CompilationUnitSpec compilationUnitSpec, BlockSpec outerBlock, String labelName) {
        super(node, compilationUnitSpec);
        this.outerBlock = outerBlock;
        this.labelName = labelName;
    }

    public void doBreak(String labelForBreaking) {
        this.broken = true;
        if (labelForBreaking == null) {
            this.fastEnd();
        }
        BlockSpec temp = this;
        while (temp != null) {
            if (temp instanceof LoopBlockSpec) {
                if (((LoopBlockSpec) temp).labelName != null && ((LoopBlockSpec) temp).labelName.equals(labelForBreaking)) {
                    temp.fastEnd();
                    return;
                } else {
                    temp.reset();
                }
                temp = ((LoopBlockSpec) temp).outerBlock;
            } else {
                return;
            }
        }
    }

    public void doContinue(String labelForContinue) {
        if (labelForContinue == null) {
            this.reset();
        }
        BlockSpec temp = this;
        while (temp != null) {
            if (temp instanceof LoopBlockSpec) {
                temp.reset();
                if (((LoopBlockSpec) temp).labelName != null && ((LoopBlockSpec) temp).labelName.equals(labelForContinue)) {
                    return;
                }
                temp = ((LoopBlockSpec) temp).outerBlock;
            } else {
                return;
            }
        }
    }


    @Override
    public boolean hasNextMethodInvocation() {
        return super.hasNextMethodInvocation();
    }

    @Override
    protected StatementScanner getScanner() {
        return new LoopStatementScanner(this);
    }

    class LoopStatementScanner extends StatementScanner {

        LoopBlockSpec outerLoop;

        public LoopStatementScanner(LoopBlockSpec outerLoop) {
            this.outerLoop = outerLoop;
        }

        @Override
        public Void visitDoWhileLoop(DoWhileLoopTree node, CompilationUnitSpec compilationUnitSpec) {
            statements.add(new DoWhileLoopSpec(node, compilationUnitSpec, outerLoop, null));
            return null;
        }

        @Override
        public Void visitWhileLoop(WhileLoopTree node, CompilationUnitSpec compilationUnitSpec) {
            statements.add(new WhileLoopSpec(node, compilationUnitSpec, outerLoop, null));
            return null;
        }

        @Override
        public Void visitForLoop(ForLoopTree node, CompilationUnitSpec compilationUnitSpec) {
            statements.add(new ForLoopSpec(node, compilationUnitSpec, outerLoop, null));
            return null;
        }

        @Override
        public Void visitLabeledStatement(LabeledStatementTree node, CompilationUnitSpec compilationUnitSpec) {
            statements.add(new LabeledStatementSpec(node, compilationUnitSpec, outerLoop));
            return null;
        }

        @Override
        public Void visitBreak(BreakTree node, CompilationUnitSpec compilationUnitSpec) {
            statements.add(new BreakStatementSpec(node, outerLoop));
            return null;
        }

        @Override
        public Void visitContinue(ContinueTree node, CompilationUnitSpec compilationUnitSpec) {
            statements.add(new ContinueStatementSpec(node, outerLoop));
            return null;
        }
    }
}
