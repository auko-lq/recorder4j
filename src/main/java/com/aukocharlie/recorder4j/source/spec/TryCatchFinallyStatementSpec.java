package com.aukocharlie.recorder4j.source.spec;

import com.aukocharlie.recorder4j.exception.RecorderRuntimeException;
import com.sun.source.tree.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author auko
 */
public class TryCatchFinallyStatementSpec implements Statement {

    BlockSpec resourceBlock;
    BlockSpec tryBlock;
    List<BlockSpec> catchBlocks;
    BlockSpec finallyBlock;

    public TryCatchFinallyStatementSpec(TryTree node, CompilationUnitSpec compilationUnitSpec) {
        List<? extends Tree> resources = node.getResources();
        List<StatementTree> resourceStatements = new ArrayList<>();
        for (Tree resource : resources) {
            if (!(resource instanceof StatementTree)) {
                throw new RecorderRuntimeException("Illegal resource type: " + resource.getClass());
            }
            resourceStatements.add((StatementTree) (resource));
        }
        this.resourceBlock = new BlockSpec(resourceStatements, compilationUnitSpec);
        this.tryBlock = new BlockSpec(node.getBlock(), compilationUnitSpec);
        this.catchBlocks = node.getCatches().stream()
                .map((catchItem) -> new BlockSpec(catchItem.getBlock(), compilationUnitSpec))
                .collect(Collectors.toList());
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
        catchBlocks.stream().map(BlockSpec::getLambdaBlockList).forEach(lambdaList::addAll);
        lambdaList.addAll(finallyBlock.getLambdaBlockList());
        return lambdaList;
    }
}
