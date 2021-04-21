package com.aukocharlie.recorder4j.source;

import com.aukocharlie.recorder4j.constant.CommonConstants;
import com.aukocharlie.recorder4j.exception.RecorderRuntimeException;
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

import static com.aukocharlie.recorder4j.constant.CommonConstants.UNKNOWN;

/**
 * @author auko
 */
public class SourceManager {

    Context context;
    JavacFileManager fileManager;
    JavacTool javacTool;

    // Maven path is adopted by default
    private String srcRoot = CommonConstants.WORKING_DIR + "src/main/java";
    private final Map<String, MethodInvocationIterator> classNameMethodInvocationsMap = new HashMap<>();

    public SourceManager() {
        init();
    }

    private void init() {
        context = new Context();
        fileManager = new JavacFileManager(context, true, Charset.defaultCharset());
        javacTool = JavacTool.create();
    }

    public void setSrcRoot(String path) {
        this.srcRoot = path;
    }

    public void parseSourceCodeByPath(String srcRelativePath) {
        parseSourceCodeByClassName(convertResourcePathToClassName(srcRelativePath));
    }

    public void parseSourceCodeByClassName(String className) {
        if (classNameMethodInvocationsMap.containsKey(className)) {
            return;
        }
        File srcFile = new File(srcRoot, convertClassNameToPath(className));
        Iterable<? extends JavaFileObject> srcFileObjects = fileManager.getJavaFileObjects(srcFile);
        JavaCompiler.CompilationTask compilationTask = javacTool.getTask(null, fileManager, null, null, null, srcFileObjects);
        try {
            Iterable<? extends CompilationUnitTree> compilationResult = ((JavacTask) compilationTask).parse();
            Iterator<? extends CompilationUnitTree> compilationResultIterator = compilationResult.iterator();
            if (compilationResultIterator.hasNext()) {
                // A tree represents an AST (source code or package-info)
                CompilationUnitTree tree = compilationResultIterator.next();
                SourceScanner scanner = new SourceScanner();
                tree.accept(scanner, null);
                List<MethodInvocationPosition> positions = scanner.generateMethodExecChain();
                classNameMethodInvocationsMap.put(className, new MethodInvocationIterator(positions));
            }
        } catch (IOException e) {
            throw new RecorderRuntimeException(String.format("IOException occurred while parsing %s: %s", srcFile.getAbsolutePath(), e.getMessage()));
        }
    }

    public SourcePosition nextPosition(String className) {
        if (!classNameMethodInvocationsMap.containsKey(className)) {
            return SourcePosition.unknownPosition(UNKNOWN);
        }
        MethodInvocationIterator methodInvocationIterator = classNameMethodInvocationsMap.get(className);
        // TODO: If hasNext is false, check the reason or throw an exception
        return methodInvocationIterator.hasNext() ? methodInvocationIterator.next() : SourcePosition.unknownPosition(UNKNOWN);
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


    static class MethodInvocationIterator implements Iterator<MethodInvocationPosition> {

        private List<MethodInvocationPosition> positions = new ArrayList<>();

        private int currentIndex = 0;

        public MethodInvocationIterator(List<MethodInvocationPosition> positions) {
            if (positions.size() > 0) {
                for (MethodInvocationPosition position : positions) {
                    spreadOut(position);
                }
            }
        }


        @Override
        public boolean hasNext() {
            return positions.size() > currentIndex;
        }

        @Override
        public MethodInvocationPosition next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return positions.get(currentIndex++);
        }

        private void spreadOut(MethodInvocationPosition position) {
            if (position == null) {
                return;
            }
            for (MethodInvocationPosition argMethod : position.getArgMethodPosition()) {
                spreadOut(argMethod);
            }

            positions.add(position);

            if (position.nextMethod() != null) {
                spreadOut(position.nextMethod());
            }
        }
    }
}
