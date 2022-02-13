package com.aukocharlie.recorder4j.source;

import com.aukocharlie.recorder4j.source.spec.CompilationUnitSpec;
import com.github.javaparser.JavaParser;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.TreeScanner;

/**
 * @author auko
 */
public class CompilationUnitScanner extends TreeScanner<Void, JavaParser> {

    CompilationUnitSpec compilationUnitSpec;

    @Override
    public Void visitCompilationUnit(CompilationUnitTree node, JavaParser javaParser) {
        this.compilationUnitSpec = new CompilationUnitSpec(node, javaParser);
        return null;
    }

}
