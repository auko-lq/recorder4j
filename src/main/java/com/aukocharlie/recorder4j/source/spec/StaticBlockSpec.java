package com.aukocharlie.recorder4j.source.spec;


import com.sun.source.tree.BlockTree;

/**
 * @author auko
 */
public class StaticBlockSpec extends BlockSpec implements StaticInitializer {

    public StaticBlockSpec(BlockTree node, CompilationUnitSpec compilationUnitSpec) {
        super(node, compilationUnitSpec);
    }

}
