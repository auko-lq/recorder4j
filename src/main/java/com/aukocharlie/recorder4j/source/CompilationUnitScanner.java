package com.aukocharlie.recorder4j.source;

import com.aukocharlie.recorder4j.source.spec.CompilationUnitSpec;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.Tree;

import java.util.HashMap;
import java.util.Map;

/**
 * @author auko
 */
public class CompilationUnitScanner extends SourceScanner {

    CompilationUnitSpec compilationUnitSpec;

    @Override
    public Void visitCompilationUnit(CompilationUnitTree node, CompilationUnitSpec v) {
        this.compilationUnitSpec = new CompilationUnitSpec(node);
        return null;
    }

}
