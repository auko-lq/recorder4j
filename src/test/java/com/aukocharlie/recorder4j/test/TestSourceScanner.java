package com.aukocharlie.recorder4j.test;

import com.aukocharlie.recorder4j.constant.CommonConstants;
import com.aukocharlie.recorder4j.source.CompilationUnitScanner;
import com.aukocharlie.recorder4j.source.SourceScanner;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.JavacTask;
import com.sun.tools.javac.api.JavacTool;
import com.sun.tools.javac.file.JavacFileManager;
import com.sun.tools.javac.util.Context;
import org.junit.Before;
import org.junit.Test;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @author auko
 */
public class TestSourceScanner {

    StringBuilder sb;
    Context context = new Context();
    JavacFileManager fileManager = new JavacFileManager(context, true, Charset.defaultCharset());
    JavacTool javacTool = new JavacTool();

    @Before
    public void init() {
        sb = new StringBuilder();
    }

    @Test
    public void testChainCase() {
        String[] path = new String[]{CommonConstants.WORKING_DIR + "src\\test\\java\\com\\aukocharlie\\recorder4j\\ChainCase.java"};
        Iterable<? extends JavaFileObject> files = fileManager.getJavaFileObjects(path);

        JavaCompiler.CompilationTask compilationTask = javacTool.getTask(null, fileManager, null, null, null, files);
        JavacTask javacTask = (JavacTask) compilationTask;
        try {
            Iterable<? extends CompilationUnitTree> result = javacTask.parse();
            for (CompilationUnitTree tree : result) {
                CompilationUnitScanner scanner = new CompilationUnitScanner();
                tree.accept(scanner, null);
                System.out.println();
//                Assert.assertEquals("new ChainCase(\"0\"): 16:31 -> 16:49\n" +
//                        "chain(\"2\"): 17:18 -> 17:28\n" +
//                        "new ChainCase(\"3\"): 18:24 -> 18:42\n" +
//                        "test(): 18:43 -> 18:49\n" +
//                        "new ChainCase(\"4\"): 18:51 -> 18:69\n" +
//                        "test(): 18:70 -> 18:76\n" +
//                        "chain(new ChainCase(\"3\").test(), new ChainCase(\"4\").test()): 18:18 -> 18:77\n" +
//                        "chain(\"5\"): 19:18 -> 19:28\n" +
//                        "System.out.println(chainCase.str): 20:9 -> 20:42\n", sb.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLambdaCase(){
        String[] path = new String[]{CommonConstants.WORKING_DIR + "src\\test\\java\\com\\aukocharlie\\recorder4j\\LambdaCase.java"};
        Iterable<? extends JavaFileObject> files = fileManager.getJavaFileObjects(path);

        JavaCompiler.CompilationTask compilationTask = javacTool.getTask(null, fileManager, null, null, null, files);
        JavacTask javacTask = (JavacTask) compilationTask;
        try {
            Iterable<? extends CompilationUnitTree> result = javacTask.parse();
            for (CompilationUnitTree tree : result) {
                CompilationUnitScanner scanner = new CompilationUnitScanner();
                tree.accept(scanner, null);
                System.out.println(sb.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testTwoClassCase(){
        String[] path = new String[]{CommonConstants.WORKING_DIR + "src\\test\\java\\com\\aukocharlie\\recorder4j\\TwoClassesCase.java"};
        Iterable<? extends JavaFileObject> files = fileManager.getJavaFileObjects(path);

        JavaCompiler.CompilationTask compilationTask = javacTool.getTask(null, fileManager, null, null, null, files);
        JavacTask javacTask = (JavacTask) compilationTask;
        try {
            Iterable<? extends CompilationUnitTree> result = javacTask.parse();
            for (CompilationUnitTree tree : result) {
                SourceScanner scanner = new SourceScanner();
                tree.accept(scanner, null);
                System.out.println(sb.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInnerClass(){
        String[] path = new String[]{CommonConstants.WORKING_DIR + "src\\test\\java\\com\\aukocharlie\\recorder4j\\InnerClassCase.java"};
        Iterable<? extends JavaFileObject> files = fileManager.getJavaFileObjects(path);

        JavaCompiler.CompilationTask compilationTask = javacTool.getTask(null, fileManager, null, null, null, files);
        JavacTask javacTask = (JavacTask) compilationTask;
        try {
            Iterable<? extends CompilationUnitTree> result = javacTask.parse();
            for (CompilationUnitTree tree : result) {
                SourceScanner scanner = new SourceScanner();
                tree.accept(scanner, null);
                System.out.println(sb.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testStaticBlockCase(){
        String[] path = new String[]{CommonConstants.WORKING_DIR + "src\\test\\java\\com\\aukocharlie\\recorder4j\\StaticBlockCase.java"};
        Iterable<? extends JavaFileObject> files = fileManager.getJavaFileObjects(path);

        JavaCompiler.CompilationTask compilationTask = javacTool.getTask(null, fileManager, null, null, null, files);
        JavacTask javacTask = (JavacTask) compilationTask;
        try {
            Iterable<? extends CompilationUnitTree> result = javacTask.parse();
            for (CompilationUnitTree tree : result) {
                SourceScanner scanner = new SourceScanner();
                tree.accept(scanner, null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testIfElseCase(){
        String[] path = new String[]{CommonConstants.WORKING_DIR + "src\\test\\java\\com\\aukocharlie\\recorder4j\\IfElseCase.java"};
        Iterable<? extends JavaFileObject> files = fileManager.getJavaFileObjects(path);

        JavaCompiler.CompilationTask compilationTask = javacTool.getTask(null, fileManager, null, null, null, files);
        JavacTask javacTask = (JavacTask) compilationTask;
        try {
            Iterable<? extends CompilationUnitTree> result = javacTask.parse();
            for (CompilationUnitTree tree : result) {
                SourceScanner scanner = new SourceScanner();
                tree.accept(scanner, null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTryCatchCase(){
        String[] path = new String[]{CommonConstants.WORKING_DIR + "src\\test\\java\\com\\aukocharlie\\recorder4j\\TryCatchCase.java"};
        Iterable<? extends JavaFileObject> files = fileManager.getJavaFileObjects(path);

        JavaCompiler.CompilationTask compilationTask = javacTool.getTask(null, fileManager, null, null, null, files);
        JavacTask javacTask = (JavacTask) compilationTask;
        try {
            Iterable<? extends CompilationUnitTree> result = javacTask.parse();
            for (CompilationUnitTree tree : result) {
                SourceScanner scanner = new SourceScanner();
                tree.accept(scanner, null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
