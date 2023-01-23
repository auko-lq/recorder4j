package com.aukocharlie.recorder4j.source.spec.block;

import com.aukocharlie.recorder4j.source.scanner.StatementScanner;
import com.aukocharlie.recorder4j.source.spec.*;
import com.aukocharlie.recorder4j.source.spec.statement.*;
import com.sun.source.tree.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author auko
 */
public abstract class AbstractBlockSpec extends AbstractMethodInvocationIterator implements LambdaPlaceable {

    /**
     * There are three cases for the value:
     * <p>1. When the block is located in static, then the value is <em>static</em>.
     * <p>2. When the block is located in a method block, the value is the method name.
     * <p>3. When the block is located in a lambda block, the value is <em>null</em>.
     */
    public String name;

    List<Statement> statements = new ArrayList<>();
    int currentStatementIndex = 0;

    boolean returned = false;

    public AbstractBlockSpec(StatementTree node, CompilationUnitSpec compilationUnitSpec, String name) {
        this.name = name;
        if (node == null) {
            return;
        }

        StatementScanner scanner = getScanner();
        if (node instanceof BlockTree) {
            scanner.scan(((BlockTree) node).getStatements(), compilationUnitSpec);
        } else {
            // While statement is not a block, treat it as a block with only one statement
            scanner.scan(node, compilationUnitSpec);
        }
        statements = scanner.getStatements();
    }

    public AbstractBlockSpec(StatementTree node, CompilationUnitSpec compilationUnitSpec) {
        this(node, compilationUnitSpec, null);
    }

    public AbstractBlockSpec(List<? extends StatementTree> nodes, CompilationUnitSpec compilationUnitSpec, String name) {
        this.name = name;
        if (nodes == null) {
            return;
        }
        getScanner().scan(nodes, compilationUnitSpec);
    }

    public AbstractBlockSpec(List<? extends StatementTree> nodes, CompilationUnitSpec compilationUnitSpec) {
        this(nodes, compilationUnitSpec, null);
    }

    @Override
    public List<AbstractBlockSpec> getLambdaBlockList() {
        List<AbstractBlockSpec> lambdaList = new ArrayList<>();
        for (Statement statement : statements) {
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
        if (statements != null) {
            nodeInExecutionOrder.addAll(statements);
        }
    }

    protected StatementScanner getScanner() {
        return new StatementScanner();
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
        return this.returned || this.currentStatementIndex == this.statements.size();
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