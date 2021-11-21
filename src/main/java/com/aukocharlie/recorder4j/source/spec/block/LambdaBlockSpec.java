package com.aukocharlie.recorder4j.source.spec.block;

import com.aukocharlie.recorder4j.source.spec.CompilationUnitSpec;
import com.sun.source.tree.BlockTree;

import java.util.List;

/**
 * @author auko
 */
public class LambdaBlockSpec extends MethodBlockSpec {
    public LambdaBlockSpec(BlockTree node, CompilationUnitSpec compilationUnitSpec) {
        super(node, compilationUnitSpec, null);
    }


    @Override
    public List<AbstractBlockSpec> getLambdaBlockList() {
        List<AbstractBlockSpec> lambdaBlocks = super.getLambdaBlockList();
        lambdaBlocks.forEach((blockSpec -> blockSpec.name = "null"));
        return lambdaBlocks;
    }
}
