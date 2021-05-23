package com.aukocharlie.recorder4j.source.spec;

import com.aukocharlie.recorder4j.source.SourceScanner;
import com.sun.source.tree.*;

import java.util.ArrayList;
import java.util.List;

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
    String name;

    List<Statement> statements = new ArrayList<>();
    int currentStatement = 0;

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

    protected StatementScanner getScanner() {
        return new StatementScanner();
    }

    protected void reset() {
        this.currentStatement = 0;
    }

    protected void fastEnd() {
        this.currentStatement = this.statements.size();
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
        return false;
    }

    @Override
    public MethodInvocationPosition nextMethodInvocation() {
        return null;
    }

    class StatementScanner extends SourceScanner {

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
    }

}

class MethodBlockSpec extends BlockSpec {

    public MethodBlockSpec(BlockTree node, CompilationUnitSpec compilationUnitSpec, String methodName) {
        super(node, compilationUnitSpec, methodName);
    }

}


class StaticBlockSpec extends BlockSpec implements StaticInitializer {

    public StaticBlockSpec(BlockTree node, CompilationUnitSpec compilationUnitSpec) {
        super(node, compilationUnitSpec, "static");
    }
}

class LambdaBlockSpec extends BlockSpec {
    public LambdaBlockSpec(BlockTree node, CompilationUnitSpec compilationUnitSpec) {
        super(node, compilationUnitSpec, null);
    }


    @Override
    public List<BlockSpec> getLambdaBlockList() {
        List<BlockSpec> lambdaBlocks = super.getLambdaBlockList();
        lambdaBlocks.forEach((blockSpec -> blockSpec.name = "null"));
        return lambdaBlocks;
    }
}

class LoopBlockSpec extends BlockSpec {

    LoopBlockSpec outerLoop;
    String labelName;

    public LoopBlockSpec(StatementTree node, CompilationUnitSpec compilationUnitSpec, LoopBlockSpec outerLoop, String labelName) {
        super(node, compilationUnitSpec);
        this.outerLoop = outerLoop;
        this.labelName = labelName;
    }

    public void doBreak(String labelForBreaking) {
        if (labelForBreaking == null) {
            this.fastEnd();
        }
        LoopBlockSpec temp = this;
        while (temp != null) {
            if (temp.labelName != null && temp.labelName.equals(labelForBreaking)) {
                temp.fastEnd();
                return;
            } else {
                this.reset();
            }
            temp = temp.outerLoop;
        }
    }

    public void doContinue(String labelForContinue) {
        if (labelForContinue == null) {
            this.reset();
        }
        LoopBlockSpec temp = this;
        while (temp != null) {
            temp.reset();
            if (temp.labelName != null && temp.labelName.equals(labelForContinue)) {
                return;
            }
            temp = temp.outerLoop;
        }
    }


    @Override
    protected StatementScanner getScanner() {
        return new LoopStatementScanner(this);
    }

    class LoopStatementScanner extends StatementScanner {

        LoopBlockSpec outerLoop;

        public LoopStatementScanner(LoopBlockSpec outerLoop) {
            this.outerLoop = outerLoop;
        }

        @Override
        public Void visitDoWhileLoop(DoWhileLoopTree node, CompilationUnitSpec compilationUnitSpec) {
            statements.add(new DoWhileLoopSpec(node, compilationUnitSpec, outerLoop, null));
            return null;
        }

        @Override
        public Void visitWhileLoop(WhileLoopTree node, CompilationUnitSpec compilationUnitSpec) {
            statements.add(new WhileLoopSpec(node, compilationUnitSpec, outerLoop, null));
            return null;
        }

        @Override
        public Void visitForLoop(ForLoopTree node, CompilationUnitSpec compilationUnitSpec) {
            statements.add(new ForLoopSpec(node, compilationUnitSpec, outerLoop, null));
            return null;
        }

        @Override
        public Void visitLabeledStatement(LabeledStatementTree node, CompilationUnitSpec compilationUnitSpec) {
            statements.add(new LabeledStatementSpec(node, compilationUnitSpec, outerLoop));
            return null;
        }

        @Override
        public Void visitBreak(BreakTree node, CompilationUnitSpec compilationUnitSpec) {
            statements.add(new BreakStatementSpec(node, outerLoop));
            return null;
        }

        @Override
        public Void visitContinue(ContinueTree node, CompilationUnitSpec compilationUnitSpec) {
            statements.add(new ContinueStatementSpec(node, outerLoop));
            return null;
        }
    }
}