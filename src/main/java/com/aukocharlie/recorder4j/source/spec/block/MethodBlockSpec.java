package com.aukocharlie.recorder4j.source.spec.block;

import com.aukocharlie.recorder4j.source.scanner.MethodBlockScanner;
import com.aukocharlie.recorder4j.source.scanner.BlockScanner;
import com.aukocharlie.recorder4j.source.spec.CompilationUnitSpec;
import com.sun.source.tree.*;

/**
 * @author auko
 */
public class MethodBlockSpec extends AbstractBlockSpec {

    public MethodBlockSpec(BlockTree node, CompilationUnitSpec compilationUnitSpec, String methodName) {
        super(node, compilationUnitSpec, methodName);
    }

    @Override
    protected BlockScanner getScanner() {
        return new MethodBlockScanner(this);
    }
}
