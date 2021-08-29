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
     * Mark whether the break/continue statement is executed in the loop block.
     */
    boolean broken = false;
    boolean continued = false;

    public LoopBlockSpec(StatementTree node, CompilationUnitSpec compilationUnitSpec, BlockSpec outerBlock, String labelName) {
        super(node, compilationUnitSpec);
        this.outerBlock = outerBlock;
        this.labelName = labelName;
    }

    public void doBreak(String labelForBreaking) {
        for (BlockSpec temp = this; temp != null; ) {
            if (temp instanceof LoopBlockSpec) {
                ((LoopBlockSpec) temp).broken = true;
                if (labelForBreaking == null
                        || (((LoopBlockSpec) temp).labelName != null && ((LoopBlockSpec) temp).labelName.equals(labelForBreaking))) {
                    return;
                }
                temp = ((LoopBlockSpec) temp).outerBlock;
            } else {
                return;
            }
        }
    }

    public void doContinue(String labelForContinuing) {
        for (BlockSpec temp = this; temp != null; ) {
            if (temp instanceof LoopBlockSpec) {
                ((LoopBlockSpec) temp).continued = true;
                if (labelForContinuing == null
                        || (((LoopBlockSpec) temp).labelName != null && ((LoopBlockSpec) temp).labelName.equals(labelForContinuing))) {
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
        for (int i = currentStatementIndex; blockReturned() || blockBrokenOrContinued(); i++) {
            if (statements.get(i).hasNextMethodInvocation()) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected StatementScanner getScanner() {
        return new LoopStatementScanner(this);
    }

    @Override
    protected void reset() {
        this.currentStatementIndex = 0;
        this.broken = false;
    }

    private boolean blockBrokenOrContinued() {
        return broken || continued;
    }

    class LoopStatementScanner extends StatementScanner {

        public LoopStatementScanner(LoopBlockSpec statementLocatedBlock) {
            super(statementLocatedBlock);
        }

        @Override
        public Void visitDoWhileLoop(DoWhileLoopTree node, CompilationUnitSpec compilationUnitSpec) {
            statements.add(new DoWhileLoopSpec(node, compilationUnitSpec, (LoopBlockSpec) statementLocatedBlock, null));
            return null;
        }

        @Override
        public Void visitWhileLoop(WhileLoopTree node, CompilationUnitSpec compilationUnitSpec) {
            statements.add(new WhileLoopSpec(node, compilationUnitSpec, (LoopBlockSpec) statementLocatedBlock, null));
            return null;
        }

        @Override
        public Void visitForLoop(ForLoopTree node, CompilationUnitSpec compilationUnitSpec) {
            statements.add(new ForLoopSpec(node, compilationUnitSpec, (LoopBlockSpec) statementLocatedBlock, null));
            return null;
        }

        @Override
        public Void visitLabeledStatement(LabeledStatementTree node, CompilationUnitSpec compilationUnitSpec) {
            statements.add(new LabeledStatementSpec(node, compilationUnitSpec, (LoopBlockSpec) statementLocatedBlock));
            return null;
        }

        @Override
        public Void visitBreak(BreakTree node, CompilationUnitSpec compilationUnitSpec) {
            statements.add(new BreakStatementSpec(node, (LoopBlockSpec) statementLocatedBlock));
            return null;
        }

        @Override
        public Void visitContinue(ContinueTree node, CompilationUnitSpec compilationUnitSpec) {
            statements.add(new ContinueStatementSpec(node, (LoopBlockSpec) statementLocatedBlock));
            return null;
        }
    }
}
