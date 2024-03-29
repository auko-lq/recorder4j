package com.aukocharlie.recorder4j.source.spec;

import com.aukocharlie.recorder4j.exception.RecorderRuntimeException;
import com.aukocharlie.recorder4j.source.SourcePosition;
import com.aukocharlie.recorder4j.source.scanner.ClassScanner;
import com.aukocharlie.recorder4j.source.spec.clazz.ClassSpec;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.sun.source.tree.*;
import lombok.Data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author auko
 */
@Data
public class CompilationUnitSpec {

    private CompilationUnitTree compilationUnitTree;
    private String sourceCode;
    private LineMap lineMap;
    private String packageName;
    private JavaParser javaParser;
    private ParseResult<CompilationUnit> parseResult;

    /**
     * example:
     * <p>
     * key: CompilationUnitSpec
     * value: com.aukocharlie.recorder4j.source.spec.CompilationUnitSpec
     */
    private Map<String, String> classNameToImportMap;

    private Map<String, ClassSpec> nameToClassMap = new HashMap<>();

    public CompilationUnitSpec(CompilationUnitTree node, JavaParser javaParser) {
        compilationUnitTree = node;
        try {
            sourceCode = node.getSourceFile().getCharContent(true).toString();
        } catch (IOException e) {
            throw new RecorderRuntimeException(e.getMessage());
        }
        lineMap = node.getLineMap();
        packageName = node.getPackageName().toString();
        this.javaParser = javaParser;
        try {
            parseResult = javaParser.parse(new File(node.getSourceFile().toUri().toString().substring(6)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        new ClassScanner().visitCompilationUnit(node, this);
    }

    public SourcePosition getSourcePosition(Tree node) {
        return SourcePosition.getSourcePosition(compilationUnitTree, node, lineMap);
    }
}
