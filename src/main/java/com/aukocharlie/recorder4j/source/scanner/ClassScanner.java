package com.aukocharlie.recorder4j.source.scanner;

import com.aukocharlie.recorder4j.source.SourceScanner;
import com.aukocharlie.recorder4j.source.spec.clazz.ClassSpec;
import com.aukocharlie.recorder4j.source.spec.CompilationUnitSpec;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;

public class ClassScanner extends SourceScanner {

    @Override
    public Void visitCompilationUnit(CompilationUnitTree node, CompilationUnitSpec c) {
        return super.visitCompilationUnit(node, c);
    }

    @Override
    public Void visitClass(ClassTree node, CompilationUnitSpec c) {
        String classFullName = (c.getPackageName() != null && c.getPackageName().length() > 0)
                ? c.getPackageName() + "." + node.getSimpleName()
                : node.getSimpleName().toString();
        ClassSpec classSpec = new ClassSpec(node, classFullName, c);
        c.getNameToClassMap().put(classFullName, classSpec);
        return null;
    }
}