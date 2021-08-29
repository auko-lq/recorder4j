package com.aukocharlie.recorder4j.source.spec.statement;

import com.aukocharlie.recorder4j.exception.RecorderRuntimeException;
import com.aukocharlie.recorder4j.source.spec.*;
import com.aukocharlie.recorder4j.source.spec.block.BlockSpec;
import com.aukocharlie.recorder4j.source.spec.expression.Expression;
import com.aukocharlie.recorder4j.source.spec.expression.MethodInvocationExpressionSpec;
import com.sun.source.tree.*;

import java.util.*;

/**
 * @author auko
 */
public class TryCatchFinallyStatementSpec implements Statement {

    BlockSpec resourceBlock;
    BlockSpec tryBlock;
    LinkedHashMap<UninitializedDeclarationStatementSpec, BlockSpec> catchBlocks;
    BlockSpec finallyBlock;

    public TryCatchFinallyStatementSpec(TryTree node, CompilationUnitSpec compilationUnitSpec) {
        List<? extends Tree> resources = Optional.ofNullable(node.getResources()).orElse(Collections.emptyList());
        List<StatementTree> resourceStatements = new ArrayList<>();
        for (Tree resource : resources) {
            if (!(resource instanceof StatementTree)) {
                throw new RecorderRuntimeException("Illegal resource type: " + resource.getClass());
            }
            resourceStatements.add((StatementTree) (resource));
        }
        this.resourceBlock = new BlockSpec(resourceStatements, compilationUnitSpec);
        this.tryBlock = new BlockSpec(node.getBlock(), compilationUnitSpec);
        if (!Objects.isNull(node.getCatches())) {
            this.catchBlocks = new LinkedHashMap<>();
            for (CatchTree catchItem : node.getCatches()) {
                this.catchBlocks.put(new UninitializedDeclarationStatementSpec(catchItem.getParameter().getType()),
                        new BlockSpec(catchItem.getBlock(), compilationUnitSpec));
            }
        }
        this.finallyBlock = new BlockSpec(node.getFinallyBlock(), compilationUnitSpec);
    }

    @Override
    public Expression nextExpression() {
        return null;
    }

    @Override
    public List<BlockSpec> getLambdaBlockList() {
        List<BlockSpec> lambdaList = new ArrayList<>();
        lambdaList.addAll(resourceBlock.getLambdaBlockList());
        lambdaList.addAll(tryBlock.getLambdaBlockList());
        catchBlocks.values().stream().distinct().map(BlockSpec::getLambdaBlockList).forEach(lambdaList::addAll);
        lambdaList.addAll(finallyBlock.getLambdaBlockList());
        return lambdaList;
    }

    @Override
    public boolean hasNextMethodInvocation() {
        if (resourceBlock.hasNextMethodInvocation()) {
            return true;
        }
        resourceBlock.reset();

        if (tryBlock.hasNextMethodInvocation()) {
            return true;
        }
        tryBlock.reset();

        if (finallyBlock.hasNextMethodInvocation()) {
            return true;
        }
        finallyBlock.reset();
        return false;
    }

    @Override
    public MethodInvocationExpressionSpec nextMethodInvocation() {
        if (resourceBlock.hasNextMethodInvocation()) {
            return resourceBlock.nextMethodInvocation();
        }
        if (tryBlock.hasNextMethodInvocation()) {
            return tryBlock.nextMethodInvocation();
        }
        if (finallyBlock.hasNextMethodInvocation()) {
            return finallyBlock.nextMethodInvocation();
        }
        throw new NoSuchElementException("There isn't next method invocation");
    }
}
