package com.aukocharlie.recorder4j.source.spec;

import com.aukocharlie.recorder4j.exception.RecorderRuntimeException;
import com.aukocharlie.recorder4j.source.Position;
import com.aukocharlie.recorder4j.source.SourcePosition;
import com.aukocharlie.recorder4j.source.SourceScanner;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.LineMap;
import com.sun.source.tree.Tree;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author auko
 */
public class CompilationUnitSpec {

    public CompilationUnitTree compilationUnitTree;
    public String sourceCode;
    public LineMap lineMap;
    public String packageName;

    Map<String, ClassSpec> nameToClassMap = new HashMap<>();

    public CompilationUnitSpec(CompilationUnitTree node) {
        compilationUnitTree = node;
        try {
            sourceCode = node.getSourceFile().getCharContent(true).toString();
        } catch (IOException e) {
            throw new RecorderRuntimeException(e.getMessage());
        }
        lineMap = node.getLineMap();
        packageName = node.getPackageName().toString();
        new ClassScanner().visitCompilationUnit(node, this);
    }

    public SourcePosition getSourcePosition(Tree node) {
        return SourcePosition.getSourcePosition(compilationUnitTree, node, lineMap);
    }

    public Position getStartPosition(Tree node) {
        return SourcePosition.getStartPosition(compilationUnitTree, node, lineMap);
    }

    public Position getEndPosition(Tree node) {
        return SourcePosition.getEndPosition(compilationUnitTree, node, lineMap);
    }

    class ClassScanner extends SourceScanner {

        @Override
        public Void visitCompilationUnit(CompilationUnitTree node, CompilationUnitSpec c) {
            return super.visitCompilationUnit(node, c);
        }

        @Override
        public Void visitClass(ClassTree node, CompilationUnitSpec c) {
            String classFullName = (packageName != null && packageName.length() > 0)
                    ? packageName + "." + node.getSimpleName()
                    : node.getSimpleName().toString();
            ClassSpec classSpec = new ClassSpec(node, classFullName, c);
            nameToClassMap.put(classFullName, classSpec);
            return null;
        }
    }
}
