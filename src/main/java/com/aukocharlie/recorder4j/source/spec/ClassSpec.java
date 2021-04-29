package com.aukocharlie.recorder4j.source.spec;

import com.aukocharlie.recorder4j.source.SourceScanner;
import com.aukocharlie.recorder4j.source.UniqueMethod;
import com.sun.source.tree.BlockTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.VariableTree;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author auko
 */
public class ClassSpec {

    String className;
    List<StaticInitializer> staticInitializers = new ArrayList<>();

    /**
     * lambda block has a method name like <em>lambda$methodName$0</em>
     * where <em>methodName</em> refers to the method name of a method
     * where the lambda block is located. If the lambda block is in a static block,
     * methodName will be <em>static</em>. The last number 0 indicates
     * the index of the lambda block in the class.
     */
    Map<UniqueMethod, BlockSpec> lambdaBlocks = new HashMap<>();

    Map<UniqueMethod, BlockSpec> methodBlocks = new HashMap<>();

    public ClassSpec(ClassTree node, String className, CompilationUnitSpec compilationUnit) {
        this.className = className;
        new MembersScanner().scan(node.getMembers(), compilationUnit);
    }

    class MembersScanner extends SourceScanner {

        /**
         * When the inner class is scanned, this method will be called
         */
        @Override
        public Void visitClass(ClassTree node, CompilationUnitSpec compilationUnitSpec) {
            String innerClassName = className + "$" + node.getSimpleName();
            ClassSpec classSpec = new ClassSpec(node, innerClassName, compilationUnitSpec);
            compilationUnitSpec.nameToClassMap.put(innerClassName, classSpec);
            return null;
        }


        @Override
        public Void visitVariable(VariableTree node, CompilationUnitSpec compilationUnitSpec) {
            StaticFieldInitializerSpec staticFieldinItializerSpec = new StaticFieldInitializerSpec(node, compilationUnitSpec);
            staticInitializers.add(staticFieldinItializerSpec);
            for (BlockSpec lambdaBlock : staticFieldinItializerSpec.getLambdaBlockList()) {
                addLambdaBlock(String.format("lambda$static$%s", lambdaBlocks.size()), lambdaBlock);
            }
            return null;
        }

        public Void visitMethod(MethodTree node, CompilationUnitSpec compilationUnitSpec) {
            List<String> parameterTypes = node.getTypeParameters().stream().map(Object::toString).collect(Collectors.toList());
            BlockSpec methodBlock = new BlockSpec(node.getBody(), compilationUnitSpec);

            for (BlockSpec lambdaBlock : methodBlock.getLambdaBlockList()) {
                addLambdaBlock(String.format("lambda$%s$%s", node.getName().toString(), lambdaBlocks.size()), lambdaBlock);
            }

            UniqueMethod method = new UniqueMethod(className, node.getName().toString(), parameterTypes);
            methodBlocks.put(method, methodBlock);
            return null;
        }

        @Override
        public Void visitBlock(BlockTree node, CompilationUnitSpec compilationUnitSpec) {
            if (node.isStatic()) {
                StaticBlockSpec staticBlockSpec = new StaticBlockSpec(node, compilationUnitSpec);
                staticInitializers.add(staticBlockSpec);
                for (BlockSpec lambdaBlock : staticBlockSpec.getLambdaBlockList()) {
                    addLambdaBlock(String.format("lambda$static$%s", lambdaBlocks.size()), lambdaBlock);
                }
            }
            return null;
        }

        private void addLambdaBlock(String methodName, BlockSpec BlockSpec) {
            UniqueMethod method = new UniqueMethod(className, methodName, Collections.emptyList());
            lambdaBlocks.put(method, BlockSpec);
        }

    }

}
