package com.aukocharlie.recorder4j.source;

import com.aukocharlie.recorder4j.source.spec.CompilationUnitSpec;
import com.sun.source.tree.*;
import com.sun.source.util.TreeScanner;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author auko
 * <p>
 * Scanner for a single source file
 */
public class SourceScanner extends TreeScanner<Void, CompilationUnitSpec> {

    @Override
    public Void scan(Tree node, CompilationUnitSpec v) {
        if (node != null) {
            System.out.println(node.getKind() + ": " + node);
        }
        return super.scan(node, v);
    }

}
