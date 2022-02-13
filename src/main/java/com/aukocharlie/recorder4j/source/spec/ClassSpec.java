package com.aukocharlie.recorder4j.source.spec;

import com.aukocharlie.recorder4j.source.MethodMetadata;
import com.aukocharlie.recorder4j.source.SourceScanner;
import com.aukocharlie.recorder4j.source.spec.block.AbstractBlockSpec;
import com.aukocharlie.recorder4j.source.spec.block.MethodBlockSpec;
import com.aukocharlie.recorder4j.source.spec.block.StaticBlockSpec;
import com.aukocharlie.recorder4j.source.spec.statement.StaticFieldInitializerSpec;
import com.aukocharlie.recorder4j.source.spec.statement.StaticInitializer;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedConstructorDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.sun.source.tree.BlockTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.VariableTree;
import lombok.Data;

import java.util.*;

/**
 * @author auko
 */
@Data
public class ClassSpec {

    String className;
    String simpleClassName;
    List<StaticInitializer> staticInitializers = new ArrayList<>();

    /**
     * lambda block has a method name like <em>lambda$methodName$0</em>
     * where <em>methodName</em> refers to the method name of a method
     * where the lambda block is located. If the lambda block is in a static block,
     * methodName will be <em>static</em>. The last number 0 indicates
     * the index of the lambda block in the class.
     */
    Map<MethodMetadata, AbstractBlockSpec> lambdaBlocks = new HashMap<>();

    Map<MethodMetadata, AbstractBlockSpec> methodBlocks = new HashMap<>();

    public ClassSpec(ClassTree node, String className, CompilationUnitSpec compilationUnit) {
        this.className = className;
        this.simpleClassName = className.substring(className.lastIndexOf('.') + 1);
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
            for (AbstractBlockSpec lambdaBlock : staticFieldinItializerSpec.getLambdaBlockList()) {
                addLambdaBlock(String.format("lambda$%s$%s", lambdaBlock.name, lambdaBlocks.size()), lambdaBlock);
            }
            return null;
        }

        public Void visitMethod(MethodTree node, CompilationUnitSpec compilationUnitSpec) {
            List<String> parameterTypes = new ArrayList<>(node.getParameters().size());
            compilationUnitSpec.getParseResult().getResult().flatMap(compilationUnit -> compilationUnit.getClassByName(simpleClassName)).ifPresent(classOrInterfaceDeclaration -> {
                classOrInterfaceDeclaration.findAll(ConstructorDeclaration.class, constructorDeclaration -> {
                    if (!node.getName().toString().equals("<init>")) {
                        return false;
                    }

                    for (int i = 0; i < constructorDeclaration.getParameters().size(); i++) {
                        if (!constructorDeclaration.getParameters().get(i).toString().equals(node.getParameters().get(i).toString())) {
                            return false;
                        }
                    }
                    return true;
                }).forEach(constructorDeclaration -> {
                    ResolvedConstructorDeclaration resolvedConstructorDeclaration = constructorDeclaration.resolve();
                    for (int i = 0; i < node.getParameters().size(); i++) {
                        parameterTypes.add(resolvedConstructorDeclaration.getParam(i).describeType());
                    }
                });
            });

            compilationUnitSpec.getParseResult().getResult().flatMap(compilationUnit -> compilationUnit.getClassByName(simpleClassName)).ifPresent(classOrInterfaceDeclaration -> {
                classOrInterfaceDeclaration.findAll(MethodDeclaration.class, methodDeclaration -> {
                    if (!node.getName().toString().equals(methodDeclaration.getName().toString())) {
                        return false;
                    }

                    for (int i = 0; i < methodDeclaration.getParameters().size(); i++) {
                        if (!methodDeclaration.getParameters().get(i).toString().equals(node.getParameters().get(i).toString())) {
                            return false;
                        }
                    }
                    return true;
                }).forEach(methodDeclaration -> {
                    ResolvedMethodDeclaration resolvedMethodDeclaration = methodDeclaration.resolve();
                    for (int i = 0; i < node.getParameters().size(); i++) {
                        parameterTypes.add(resolvedMethodDeclaration.getParam(i).describeType());
                    }
                });
            });
            AbstractBlockSpec methodBlock = new MethodBlockSpec(node.getBody(), compilationUnitSpec, node.getName().toString());

            for (AbstractBlockSpec lambdaBlock : methodBlock.getLambdaBlockList()) {
                addLambdaBlock(String.format("lambda$%s$%s", lambdaBlock.name, lambdaBlocks.size()), lambdaBlock);
            }

            MethodMetadata method = new MethodMetadata(className, node.getName().toString(), parameterTypes);
            methodBlocks.put(method, methodBlock);
            return null;
        }

        @Override
        public Void visitBlock(BlockTree node, CompilationUnitSpec compilationUnitSpec) {
            if (node.isStatic()) {
                StaticBlockSpec staticBlockSpec = new StaticBlockSpec(node, compilationUnitSpec);
                staticInitializers.add(staticBlockSpec);
                for (AbstractBlockSpec lambdaBlock : staticBlockSpec.getLambdaBlockList()) {
                    addLambdaBlock(String.format("lambda$%s$%s", lambdaBlock.name, lambdaBlocks.size()), lambdaBlock);
                }
            }
            return null;
        }

        private void addLambdaBlock(String methodName, AbstractBlockSpec BlockSpec) {
            MethodMetadata method = new MethodMetadata(className, methodName, Collections.emptyList());
            lambdaBlocks.put(method, BlockSpec);
        }

    }

}
