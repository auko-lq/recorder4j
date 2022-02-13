package com.aukocharlie.recorder4j.source;

import com.aukocharlie.recorder4j.constant.CommonConstants;
import com.aukocharlie.recorder4j.exception.RecorderRuntimeException;
import com.aukocharlie.recorder4j.source.spec.ClassSpec;
import com.aukocharlie.recorder4j.source.spec.block.AbstractBlockSpec;
import com.aukocharlie.recorder4j.util.TypeSolverUtils;
import com.github.javaparser.JavaParser;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.JavacTask;
import com.sun.tools.javac.api.JavacTool;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.file.JavacFileManager;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.ParserConfiguration;

/**
 * @author auko
 */
public class SourceManager {

    Context context;
    JavacFileManager fileManager;
    JavacTool javacTool;

    private TypeSolver typeSolver;
    private JavaParser javaParser;

    // Maven path is adopted by default
    private String srcRoot = CommonConstants.WORKING_DIR + "src/main/java";

    private Map<String, ClassSpec> classNameToSpecMap = new HashMap<>();

    public SourceManager() {
        init();
    }

    private void init() {
        context = new Context();
        fileManager = new JavacFileManager(context, true, Charset.defaultCharset());
        javacTool = JavacTool.create();
    }

    private void initJavaParser() throws IOException {
        typeSolver = TypeSolverUtils.generateTypeSolver(srcRoot);
        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver);
        ParserConfiguration configuration = new ParserConfiguration();
        configuration.setSymbolResolver(symbolSolver);
        javaParser = new JavaParser(configuration);
    }

    public void setSrcRoot(String path) {
        this.srcRoot = path;
    }

    public void parseSourceCodeByPath(String srcRelativePath) {
        parseSourceCodeByClassName(convertResourcePathToClassName(srcRelativePath));
    }

    // TODO: parse source code
    public void parseSourceCodeByClassName(String className) {
//        if (classNameMethodInvocationsMap.containsKey(className)) {
//            return;
//        }

        System.out.println("Ready to parse source code: " + className);
        File srcFile = new File(srcRoot, convertClassNameToPath(className));
        Iterable<? extends JavaFileObject> srcFileObjects = fileManager.getJavaFileObjects(srcFile);
        JavaCompiler.CompilationTask compilationTask = javacTool.getTask(null, fileManager, null, null, null, srcFileObjects);
        try {
            if (javaParser == null) {
                initJavaParser();
            }
            Iterable<? extends CompilationUnitTree> compilationResult = ((JavacTask) compilationTask).parse();
            Iterator<? extends CompilationUnitTree> compilationResultIterator = compilationResult.iterator();
            if (compilationResultIterator.hasNext()) {
                // A tree represents an AST (source code or package-info)
                CompilationUnitTree tree = compilationResultIterator.next();
                CompilationUnitScanner scanner = new CompilationUnitScanner();
                tree.accept(scanner, javaParser);
                classNameToSpecMap.putAll(scanner.compilationUnitSpec.getNameToClassMap());
            }
        } catch (IOException e) {
            throw new RecorderRuntimeException(String.format("IOException occurred while parsing %s: %s", srcFile.getAbsolutePath(), e.getMessage()));
        }
    }

    public SourcePosition nextMethodInvocationPosition(MethodMetadata methodMetadata) {
        ClassSpec classSpec = this.classNameToSpecMap.get(methodMetadata.getClassName());
        if (classSpec == null) {
            return SourcePosition.unknownPosition(methodMetadata.getMethodName());
        }
        AbstractBlockSpec methodBlock = classSpec.getMethodBlocks().get(methodMetadata);
        if (methodBlock != null) {
            if (methodBlock.hasNextMethodInvocation()) {
                return methodBlock.nextMethodInvocation().getMethodInvocationPosition();
            } else {
                throw new RecorderRuntimeException("Unknown method invocation");
            }
        }

        AbstractBlockSpec lambdaMethodBlock = classSpec.getLambdaBlocks().get(methodMetadata);
        if (lambdaMethodBlock != null) {
            if (lambdaMethodBlock.hasNextMethodInvocation()) {
                return lambdaMethodBlock.nextMethodInvocation().getMethodInvocationPosition();
            } else {
                throw new RecorderRuntimeException("Unknown method invocation");
            }
        }
        return SourcePosition.unknownPosition(methodMetadata.getMethodName());
    }

    /**
     * example:
     * <p>
     * com/aukocharlie/recorder4j/SimpleCase.java -> com.aukocharlie.recorder4j.SimpleCase
     */
    private static String convertResourcePathToClassName(String path) {
        return path.replace(".java", "").replace(File.separatorChar, '.');
    }

    /**
     * example:
     * <p>
     * com.aukocharlie.recorder4j.SimpleCase -> com/aukocharlie/recorder4j/SimpleCase.java
     */
    private static String convertClassNameToPath(String className) {
        return className.replace('.', File.separatorChar) + ".java";
    }

    private JavaParser getJavaParser() {
        return this.javaParser;
    }


//    static class MethodInvocationIterator implements Iterator<MethodInvocationPosition> {
//
//        private List<MethodInvocationPosition> positions = new ArrayList<>();
//
//        private int currentIndex = 0;
//
//        public MethodInvocationIterator(List<MethodInvocationPosition> positions) {
//            if (positions.size() > 0) {
//                for (MethodInvocationPosition position : positions) {
//                    spreadOut(position);
//                }
//            }
//        }
//
//
//        @Override
//        public boolean hasNext() {
//            return positions.size() > currentIndex;
//        }
//
//        @Override
//        public MethodInvocationPosition next() {
//            if (!hasNext()) {
//                throw new NoSuchElementException();
//            }
//            return positions.get(currentIndex++);
//        }
//
//        private void spreadOut(MethodInvocationPosition position) {
//            if (position == null) {
//                return;
//            }
//            for (MethodInvocationPosition argMethod : position.getArgMethodPosition()) {
//                spreadOut(argMethod);
//            }
//
//            positions.add(position);
//
//            if (position.nextMethod() != null) {
//                spreadOut(position.nextMethod());
//            }
//        }
//    }
}
