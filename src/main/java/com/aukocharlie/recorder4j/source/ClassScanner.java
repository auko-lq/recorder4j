package com.aukocharlie.recorder4j.source;

import com.aukocharlie.recorder4j.exception.RecorderRuntimeException;
import com.sun.source.tree.BlockTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.LambdaExpressionTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.util.TreeScanner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.sun.source.tree.LambdaExpressionTree.BodyKind.EXPRESSION;

/**
 * @author auko
 */
public class ClassScanner extends TreeScanner<Void, Void> {

    SourceScanner sourceScanner;
    String className;

    // Map<uniqueMethod, MethodBlockSpec>
    Map<UniqueMethod, StatementScanner> methodBlockScanners = new HashMap<>();
    List<StatementScanner> staticBlockScanners = new ArrayList<>();

    /**
     * lambda block has a method name like <em>lambda$methodName$0</em>
     * where <em>methodName</em> refers to the method name of a method
     * where the lambda block is located. If the lambda block is in a static block,
     * methodName will be <em>static</em>. The last number 0 indicates
     * the index of the lambda block in the class.
     */
    Map<UniqueMethod, StatementScanner> lambdaBlockScanners = new HashMap<>();
    int lambdaIndex = 0;

    public ClassScanner(SourceScanner sourceScanner, String className) {
        this.sourceScanner = sourceScanner;
        this.className = className;
    }

    /**
     * When the inner class is scanned, this method will be called
     */
    @Override
    public Void visitClass(ClassTree node, Void v) {
        String innerClassName = className + "$" + node.getSimpleName();
        ClassScanner scanner = new ClassScanner(sourceScanner, innerClassName);
//        sourceScanner.classScanners.put(innerClassName, scanner);
        return this.scan(node.getMembers(), v);
    }

    @Override
    public Void visitMethod(MethodTree node, Void v) {
        List<String> parameterTypes = node.getTypeParameters().stream().map(Object::toString).collect(Collectors.toList());
        UniqueMethod method = new UniqueMethod(className, node.getName().toString(), parameterTypes);
//        StatementScanner scanner = new StatementScanner(this);
//        methodBlockScanners.put(method, scanner);
//        return scanner.scan(node.getBody(), v);
        return null;
    }

    @Override
    public Void visitBlock(BlockTree node, Void v) {
        if (!node.isStatic()) {
            throw new RecorderRuntimeException("Scanning of non-static blocks that have not been processed");
        }

//        StatementScanner scanner = new StatementScanner(this);
//        staticBlockScanners.add(scanner);
//        return scanner.scan(node, v);
        return null;
    }

    /**
     * This method will only be called when a lambda expression appears
     * when scanning method block or static block.
     */
    public Void visitLambdaExpression(LambdaExpressionTree node, Void v, String methodName) {
        if (node.getBodyKind() == EXPRESSION) {
            return null;
        }
        String lambdaMethodName = String.format("lambda$%s$%d", methodName, lambdaIndex++);
        List<String> parameterList = node.getParameters().stream().map(Object::toString).collect(Collectors.toList());
        UniqueMethod method = new UniqueMethod(className, lambdaMethodName, parameterList);
//        StatementScanner scanner = new StatementScanner(this);
//        lambdaBlockScanners.put(method, scanner);
//        return scanner.scan(node.getBody(), v);
        return null;
    }

}
