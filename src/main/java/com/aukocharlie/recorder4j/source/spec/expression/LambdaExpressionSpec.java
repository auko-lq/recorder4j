package com.aukocharlie.recorder4j.source.spec.expression;

import com.aukocharlie.recorder4j.source.spec.CompilationUnitSpec;
import com.aukocharlie.recorder4j.source.spec.block.AbstractBlockSpec;
import com.aukocharlie.recorder4j.source.spec.block.LambdaBlockSpec;
import com.sun.source.tree.BlockTree;
import com.sun.source.tree.LambdaExpressionTree;

import java.util.ArrayList;
import java.util.List;

import static com.sun.source.tree.LambdaExpressionTree.BodyKind.STATEMENT;

/**
 * @author auko
 */
public class LambdaExpressionSpec extends AbstractExpressionSpec {

    AbstractBlockSpec lambdaBlock;

    public LambdaExpressionSpec(LambdaExpressionTree node, CompilationUnitSpec compilationUnitSpec, String originalExpr) {
        super(originalExpr);
        if (node.getBodyKind() == STATEMENT) {
            this.lambdaBlock = new LambdaBlockSpec((BlockTree) node.getBody(), compilationUnitSpec);
        }
    }

    @Override
    public List<AbstractBlockSpec> getLambdaBlockList() {
        if (lambdaBlock == null) {
            return new ArrayList<>();
        }
        // Adding lambda blocks inside the lambda block firstly, then add itself.
        List<AbstractBlockSpec> innerLambdaBlocks = lambdaBlock.getLambdaBlockList();
        innerLambdaBlocks.add(this.lambdaBlock);
        return innerLambdaBlocks;
    }

    @Override
    protected void setExecutionOrder() {
        nodeInExecutionOrder.add(lambdaBlock);
    }

    @Override
    public void reset() {
        lambdaBlock.reset();
    }
}
