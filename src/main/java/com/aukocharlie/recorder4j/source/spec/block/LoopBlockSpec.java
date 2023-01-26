package com.aukocharlie.recorder4j.source.spec.block;

import com.aukocharlie.recorder4j.source.scanner.LoopBlockScanner;
import com.aukocharlie.recorder4j.source.scanner.BlockScanner;
import com.aukocharlie.recorder4j.source.spec.CompilationUnitSpec;
import com.sun.source.tree.*;

/**
 * @author auko
 */
public class LoopBlockSpec extends AbstractBlockSpec {

    AbstractBlockSpec outerBlock;
    String labelName;

    /**
     * Mark whether the break/continue statement is executed in the loop block.
     */
    boolean broken = false;
    boolean continued = false;

    public LoopBlockSpec(StatementTree node, CompilationUnitSpec compilationUnitSpec, AbstractBlockSpec outerBlock, String labelName) {
        super(node, compilationUnitSpec);
        this.outerBlock = outerBlock;
        this.labelName = labelName;
    }

    public void doBreak(String labelForBreaking) {
        for (AbstractBlockSpec temp = this; temp != null; ) {
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
        for (AbstractBlockSpec temp = this; temp != null; ) {
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
    protected BlockScanner getScanner() {
        return new LoopBlockScanner(this);
    }
}
