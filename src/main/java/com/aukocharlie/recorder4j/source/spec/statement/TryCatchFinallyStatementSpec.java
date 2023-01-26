package com.aukocharlie.recorder4j.source.spec.statement;

import com.aukocharlie.recorder4j.exception.RecorderRuntimeException;
import com.aukocharlie.recorder4j.exception.UnsupportedStatementException;
import com.aukocharlie.recorder4j.source.spec.*;
import com.aukocharlie.recorder4j.source.spec.block.AbstractBlockSpec;
import com.aukocharlie.recorder4j.source.spec.expression.Expression;
import com.aukocharlie.recorder4j.source.spec.expression.MethodInvocationExpressionSpec;
import com.sun.source.tree.*;

import java.util.*;

/**
 * @author auko
 */
public class TryCatchFinallyStatementSpec extends AbstractStatementSpec {

    AbstractBlockSpec resourceBlock;
    AbstractBlockSpec tryBlock;
    LinkedHashMap<UninitializedDeclarationStatementSpec, AbstractBlockSpec> catchBlocks;
    AbstractBlockSpec finallyBlock;

    public TryCatchFinallyStatementSpec(TryTree node, CompilationUnitSpec compilationUnitSpec) {
        throw new UnsupportedStatementException("Try-catch statement is not supported now");
//        List<? extends Tree> resources = Optional.ofNullable(node.getResources()).orElse(Collections.emptyList());
//        List<StatementTree> resourceStatements = new ArrayList<>();
//        for (Tree resource : resources) {
//            if (!(resource instanceof StatementTree)) {
//                throw new RecorderRuntimeException("Illegal resource type: " + resource.getClass());
//            }
//            resourceStatements.add((StatementTree) (resource));
//        }
//        this.resourceBlock = new AbstractBlockSpec(resourceStatements, compilationUnitSpec);
//        this.tryBlock = new AbstractBlockSpec(node.getBlock(), compilationUnitSpec);
//        if (!Objects.isNull(node.getCatches())) {
//            this.catchBlocks = new LinkedHashMap<>();
//            for (CatchTree catchItem : node.getCatches()) {
//                this.catchBlocks.put(new UninitializedDeclarationStatementSpec(catchItem.getParameter().getType()),
//                        new AbstractBlockSpec(catchItem.getBlock(), compilationUnitSpec));
//            }
//        }
//        this.finallyBlock = new AbstractBlockSpec(node.getFinallyBlock(), compilationUnitSpec);
    }

    @Override
    public Expression nextExpression() {
        return null;
    }

    @Override
    public List<AbstractBlockSpec> getLambdaBlockList() {
        List<AbstractBlockSpec> lambdaList = new ArrayList<>();
        lambdaList.addAll(resourceBlock.getLambdaBlockList());
        lambdaList.addAll(tryBlock.getLambdaBlockList());
        catchBlocks.values().stream().distinct().map(AbstractBlockSpec::getLambdaBlockList).forEach(lambdaList::addAll);
        lambdaList.addAll(finallyBlock.getLambdaBlockList());
        return lambdaList;
    }

    @Override
    protected void setExecutionOrder() {

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
        return null;
    }

    @Override
    public void reset() {

    }
}
