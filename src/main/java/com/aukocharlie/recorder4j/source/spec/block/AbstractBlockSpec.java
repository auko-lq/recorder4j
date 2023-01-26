package com.aukocharlie.recorder4j.source.spec.block;

import com.aukocharlie.recorder4j.source.scanner.BlockScanner;
import com.aukocharlie.recorder4j.source.spec.*;
import com.aukocharlie.recorder4j.source.spec.expression.Expression;
import com.aukocharlie.recorder4j.source.spec.statement.*;
import com.sun.source.tree.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author auko
 */
public abstract class AbstractBlockSpec extends AbstractMethodInvocationIterator implements Statement {

    /**
     * There are three cases for the value:
     * <p>1. When the block is located in static, the value is <em>static</em>.
     * <p>2. When the block is located in a method block, the value is the method name.
     * <p>3. When the block is located in a lambda block, the value is <em>null</em>.
     */
    public String name;

    List<BlockStatement> blockStatements = new ArrayList<>();
    int currentStatementIndex = 0;

    boolean returned = false;

    public AbstractBlockSpec(StatementTree node, CompilationUnitSpec compilationUnitSpec, String name) {
        this.name = name;
        if (node == null) {
            return;
        }

        BlockScanner scanner = getScanner();
        if (node instanceof BlockTree) {
            scanner.scan(((BlockTree) node).getStatements(), compilationUnitSpec);
        } else {
            // While statement is not a block, treat it as a block with only one statement
            scanner.scan(node, compilationUnitSpec);
        }
        blockStatements = scanner.getBlockStatements();
    }

    public AbstractBlockSpec(StatementTree node, CompilationUnitSpec compilationUnitSpec) {
        this(node, compilationUnitSpec, null);
    }

    @Override
    public List<AbstractBlockSpec> getLambdaBlockList() {
        List<AbstractBlockSpec> lambdaList = new ArrayList<>();
        for (BlockStatement statement : blockStatements) {
            for (AbstractBlockSpec blockSpec : statement.getLambdaBlockList()) {
                if (blockSpec.name == null) {
                    // Usually just set the method name to the name of the outermost lambda block in the method block.
                    blockSpec.name = this.name;
                }
                lambdaList.add(blockSpec);
            }
        }
        return lambdaList;
    }

    @Override
    protected void setExecutionOrder() {
        if (blockStatements != null) {
            nodeInExecutionOrder.addAll(blockStatements);
        }
    }

    @Override
    public Expression nextExpression() {
        //TODO: return?
        return null;
    }

    protected BlockScanner getScanner() {
        return new BlockScanner();
    }

    /**
     * Reset to prepare to check again
     */
    @Override
    public void reset() {
        this.currentStatementIndex = 0;
        this.returned = false;
    }

    protected boolean blockReturned() {
        return this.returned || this.currentStatementIndex == this.blockStatements.size();
    }

    public void doReturn() {
        for (AbstractBlockSpec temp = this; temp != null; ) {
            temp.returned = true;
            if (temp instanceof LoopBlockSpec) {
                temp = ((LoopBlockSpec) temp).outerBlock;
            } else if (temp instanceof MethodBlockSpec) {
                return;
            }
        }
    }
}