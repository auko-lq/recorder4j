package com.aukocharlie.recorder4j.source.spec.block;

import com.aukocharlie.recorder4j.source.SourceScanner;
import com.aukocharlie.recorder4j.source.spec.*;
import com.aukocharlie.recorder4j.source.spec.expression.MethodInvocationExpressionSpec;
import com.aukocharlie.recorder4j.source.spec.statement.*;
import com.aukocharlie.recorder4j.source.spec.statement.loop.DoWhileLoopSpec;
import com.aukocharlie.recorder4j.source.spec.statement.loop.ForLoopSpec;
import com.aukocharlie.recorder4j.source.spec.statement.loop.WhileLoopSpec;
import com.aukocharlie.recorder4j.util.Assert;
import com.sun.source.tree.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author auko
 */
public class BlockSpec implements LambdaPlaceable, MethodInvocationPlaceable {

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

    public BlockSpec(StatementTree node, CompilationUnitSpec compilationUnitSpec, String name) {
        this.name = name;
        if (node == null) {
            return;
        }

        if (node instanceof BlockTree) {
            getScanner().scan(((BlockTree) node).getStatements(), compilationUnitSpec);
        } else {
            // When statement is not a block, treat it as a block with only one statement
            getScanner().scan(node, compilationUnitSpec);
        }
    }

    public BlockSpec(StatementTree node, CompilationUnitSpec compilationUnitSpec) {
        this(node, compilationUnitSpec, null);
    }

    public BlockSpec(List<? extends StatementTree> nodes, CompilationUnitSpec compilationUnitSpec, String name) {
        this.name = name;
        if (nodes == null) {
            return;
        }
        getScanner().scan(nodes, compilationUnitSpec);
    }

    public BlockSpec(List<? extends StatementTree> nodes, CompilationUnitSpec compilationUnitSpec) {
        this(nodes, compilationUnitSpec, null);
    }

    @Override
    public List<BlockSpec> getLambdaBlockList() {
        List<BlockSpec> lambdaList = new ArrayList<>();
        for (Statement statement : statements) {
            for (BlockSpec blockSpec : statement.getLambdaBlockList()) {
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
    public boolean hasNextMethodInvocation() {
        for (int i = currentStatementIndex; blockReturned(); i++) {
            if (statements.get(i).hasNextMethodInvocation()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public MethodInvocationExpressionSpec nextMethodInvocation() {
        // TODO: try to delete the pre-check here?
        if (!hasNextMethodInvocation()) {
            throw new NoSuchElementException("There isn't next method invocation");
        }

        while (currentStatementIndex < statements.size()) {
            if (statements.get(currentStatementIndex).hasNextMethodInvocation()) {
                return statements.get(currentStatementIndex).nextMethodInvocation();
            }
            currentStatementIndex++;
        }
        return null;
    }


    protected StatementScanner getScanner() {
        return new StatementScanner();
    }

    /**
     * Reset to prepare to check again
     */
    public void reset() {
        this.currentStatementIndex = 0;
        this.returned = false;
    }

    protected boolean blockReturned() {
        return this.returned || this.currentStatementIndex == this.statements.size();
    }

    public void doReturn() {
        for (BlockSpec temp = this; temp != null; ) {
            temp.returned = true;
            if (temp instanceof LoopBlockSpec) {
                temp = ((LoopBlockSpec) temp).outerBlock;
            } else if (temp instanceof MethodBlockSpec) {
                return;
            }
        }
    }

    class StatementScanner extends SourceScanner {

        BlockSpec statementLocatedBlock;

        public StatementScanner() {
        }

        public StatementScanner(BlockSpec statementLocatedBlock) {
            this.statementLocatedBlock = statementLocatedBlock;
        }

        @Override
        public Void visitVariable(VariableTree node, CompilationUnitSpec compilationUnitSpec) {
            statements.add(new InitializerOrAssignmentSpec(node, compilationUnitSpec));
            return null;
        }

        @Override
        public Void visitExpressionStatement(ExpressionStatementTree node, CompilationUnitSpec compilationUnitSpec) {
            statements.add(new ExpressionStatementSpec(node, compilationUnitSpec));
            return null;
        }

        @Override
        public Void visitDoWhileLoop(DoWhileLoopTree node, CompilationUnitSpec compilationUnitSpec) {
            statements.add(new DoWhileLoopSpec(node, compilationUnitSpec));
            return null;
        }

        @Override
        public Void visitWhileLoop(WhileLoopTree node, CompilationUnitSpec compilationUnitSpec) {
            statements.add(new WhileLoopSpec(node, compilationUnitSpec));
            return null;
        }

        @Override
        public Void visitForLoop(ForLoopTree node, CompilationUnitSpec compilationUnitSpec) {
            statements.add(new ForLoopSpec(node, compilationUnitSpec));
            return null;
        }

        @Override
        public Void visitIf(IfTree node, CompilationUnitSpec compilationUnitSpec) {
            statements.add(new IfSpec(node, compilationUnitSpec));
            return null;
        }

        @Override
        public Void visitTry(TryTree node, CompilationUnitSpec compilationUnitSpec) {
            statements.add(new TryCatchFinallyStatementSpec(node, compilationUnitSpec));
            return null;
        }

        @Override
        public Void visitLabeledStatement(LabeledStatementTree node, CompilationUnitSpec compilationUnitSpec) {
            statements.add(new LabeledStatementSpec(node, compilationUnitSpec));
            return null;
        }

        /**
         * Only {@link MethodBlockSpec} and {@link LoopBlockSpec} will have {@link ReturnStatementSpec}
         */
        @Override
        public Void visitReturn(ReturnTree node, CompilationUnitSpec compilationUnitSpec) {
            Assert.assertTrue(statementLocatedBlock instanceof MethodBlockSpec
                            || statementLocatedBlock instanceof LoopBlockSpec,
                    "Unsupported statementLocatedBlock class: " + statementLocatedBlock.getClass());
            statements.add(new ReturnStatementSpec(node, compilationUnitSpec, statementLocatedBlock));
            return null;
        }
    }

}