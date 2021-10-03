package com.aukocharlie.recorder4j.source.spec.statement;

import com.aukocharlie.recorder4j.source.spec.CompilationUnitSpec;
import com.aukocharlie.recorder4j.source.spec.block.AbstractBlockSpec;
import com.aukocharlie.recorder4j.source.spec.expression.MethodInvocationExpressionSpec;
import com.sun.source.tree.VariableTree;

import java.util.List;

/**
 * @author auko
 */
public class StaticFieldInitializerSpec extends AbstractStatementSpec implements StaticInitializer {

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
    public List<AbstractBlockSpec> getLambdaBlockList() {
        List<AbstractBlockSpec> lambdaBlock = initializer.getLambdaBlockList();
        for (AbstractBlockSpec blockSpec : lambdaBlock) {
            if (blockSpec.name == null) {
                blockSpec.name = "static";
            }
        }
        return lambdaBlock;
    }

    @Override
    public boolean hasNextMethodInvocation() {
        return initializer.hasNextMethodInvocation();
    }

    @Override
    public MethodInvocationExpressionSpec nextMethodInvocation() {
        return initializer.nextMethodInvocation();
    }

    @Override
    public void reset() {

    }
}
