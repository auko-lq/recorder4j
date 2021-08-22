package com.aukocharlie.recorder4j.source.spec.expression;

import com.aukocharlie.recorder4j.source.spec.CompilationUnitSpec;
import com.aukocharlie.recorder4j.source.spec.block.BlockSpec;
import com.aukocharlie.recorder4j.source.spec.block.LambdaBlockSpec;
import com.aukocharlie.recorder4j.source.spec.expression.ExpressionSpec;
import com.sun.source.tree.BlockTree;
import com.sun.source.tree.LambdaExpressionTree;

import java.util.ArrayList;
import java.util.List;

import static com.sun.source.tree.LambdaExpressionTree.BodyKind.STATEMENT;

/**
 * @author auko
 */
public class LambdaExpressionSpec extends ExpressionSpec {

    BlockSpec lambdaBlock;

    public LambdaExpressionSpec(LambdaExpressionTree node, CompilationUnitSpec compilationUnitSpec, String originalExpr) {
        super(originalExpr);
        if (node.getBodyKind() == STATEMENT) {
            this.lambdaBlock = new LambdaBlockSpec((BlockTree) node.getBody(), compilationUnitSpec);
        }
    }

    @Override
    public List<BlockSpec> getLambdaBlockList() {
        if (lambdaBlock == null) {
            return new ArrayList<>();
        }
        // First add lambda blocks in the lambda block, then add itself.
        List<BlockSpec> innerLambdaBlocks = lambdaBlock.getLambdaBlockList();
        innerLambdaBlocks.add(this.lambdaBlock);
        return innerLambdaBlocks;
    }
}
