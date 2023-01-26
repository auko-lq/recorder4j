package com.aukocharlie.recorder4j.source.spec.clazz;

import com.aukocharlie.recorder4j.source.MethodMetadata;
import com.aukocharlie.recorder4j.source.scanner.MembersScanner;
import com.aukocharlie.recorder4j.source.spec.CompilationUnitSpec;
import com.aukocharlie.recorder4j.source.spec.block.AbstractBlockSpec;
import com.aukocharlie.recorder4j.source.spec.block.BlockStatement;
import com.aukocharlie.recorder4j.source.spec.expression.MethodInvocationExpressionSpec;
import com.aukocharlie.recorder4j.source.spec.statement.StaticInitializer;
import com.sun.source.tree.ClassTree;
import lombok.Getter;

import java.util.*;

/**
 * @author auko
 */
public class ClassSpec implements BlockStatement {

    @Getter
    private final List<StaticInitializer> staticInitializers;

    /**
     * lambda block has a method name like <em>lambda$methodName$0</em>
     * where <em>methodName</em> refers to the method name of a method
     * where the lambda block is located. If the lambda block is in a static block,
     * methodName will be <em>static</em>. The last number 0 indicates
     * the index of the lambda block in the class.
     */
    @Getter
    private final Map<MethodMetadata, AbstractBlockSpec> lambdaBlocks;

    @Getter
    private final Map<MethodMetadata, AbstractBlockSpec> methodBlocks;

    public ClassSpec(ClassTree node, String className, CompilationUnitSpec compilationUnit) {
        MembersScanner scanner = new MembersScanner(className);
        scanner.scan(node.getMembers(), compilationUnit);

        this.staticInitializers = scanner.getStaticInitializers();
        this.lambdaBlocks = scanner.getLambdaBlocks();
        this.methodBlocks = scanner.getMethodBlocks();
    }

    // TODO
    @Override
    public List<AbstractBlockSpec> getLambdaBlockList() {
        return null;
    }

    // TODO
    @Override
    public boolean hasNextMethodInvocation() {
        return false;
    }

    // TODO
    @Override
    public MethodInvocationExpressionSpec nextMethodInvocation() {
        return null;
    }

    // TODO
    @Override
    public void reset() {

    }
}
