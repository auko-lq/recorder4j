package com.aukocharlie.recorder4j.source.spec.block;

import com.aukocharlie.recorder4j.source.scanner.MethodStatementScanner;
import com.aukocharlie.recorder4j.source.scanner.StatementScanner;
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
    protected StatementScanner getScanner() {
        return new MethodStatementScanner(this);
    }
}
