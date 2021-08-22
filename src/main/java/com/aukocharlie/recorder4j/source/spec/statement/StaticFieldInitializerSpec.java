package com.aukocharlie.recorder4j.source.spec.statement;

import com.aukocharlie.recorder4j.source.spec.CompilationUnitSpec;
import com.aukocharlie.recorder4j.source.spec.block.BlockSpec;
import com.aukocharlie.recorder4j.source.spec.expression.MethodInvocationExpressionSpec;
import com.sun.source.tree.VariableTree;

import java.util.List;

/**
 * @author auko
 */
public class StaticFieldInitializerSpec implements StaticInitializer {

    /**
     * For example:
     * <pre>
     *   <em>modifiers</em> <em>type</em> <em>name</em> = <em>initializer</em> ;
     * </pre>
     */
    InitializerOrAssignmentSpec initializer;

    public StaticFieldInitializerSpec(VariableTree node, CompilationUnitSpec compilationUnitSpec) {
        this.initializer = new InitializerOrAssignmentSpec(node, compilationUnitSpec);
    }

    @Override
    public List<BlockSpec> getLambdaBlockList() {
        List<BlockSpec> lambdaBlock = initializer.getLambdaBlockList();
        for (BlockSpec blockSpec : lambdaBlock) {
            if (blockSpec.name == null) {
                blockSpec.name = "static";
            }
        }
        return lambdaBlock;
    }

    @Override
    public boolean hasNextMethodInvocation() {
        return false;
    }

    @Override
    public MethodInvocationExpressionSpec nextMethodInvocation() {
        return null;
    }
}
