package com.aukocharlie.recorder4j.source.spec;

import com.aukocharlie.recorder4j.source.SourceScanner;
import com.sun.source.tree.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author auko
 */
public class BlockSpec implements LambdaAllowed {

    List<Statement> statements = new ArrayList<>();

    public BlockSpec(StatementTree node, CompilationUnitSpec compilationUnitSpec) {
        super();

        if (node == null) {
            return;
        }

        if (node instanceof BlockTree) {
            new StatementScanner().scan(((BlockTree) node).getStatements(), compilationUnitSpec);
        } else {
            // When statement is not a block, treat it as a block with only one statement
            new StatementScanner().scan(node, compilationUnitSpec);
        }
    }

    public BlockSpec(List<? extends StatementTree> nodes, CompilationUnitSpec compilationUnitSpec) {
        if (nodes == null) {
            return;
        }
        new StatementScanner().scan(nodes, compilationUnitSpec);
    }

    @Override
    public List<BlockSpec> getLambdaBlockList() {
        List<BlockSpec> lambdaList = new ArrayList<>();
        this.statements.forEach(statement -> lambdaList.addAll(statement.getLambdaBlockList()));
        return lambdaList;
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
            statements.add(new DoWhileLoopStatementSpec(node, compilationUnitSpec));
            return null;
        }

        @Override
        public Void visitWhileLoop(WhileLoopTree node, CompilationUnitSpec compilationUnitSpec) {
            statements.add(new WhileLoopStatementSpec(node, compilationUnitSpec));
            return super.visitWhileLoop(node, compilationUnitSpec);
        }

        @Override
        public Void visitForLoop(ForLoopTree node, CompilationUnitSpec compilationUnitSpec) {
            statements.add(new ForLoopStatementSpec(node, compilationUnitSpec));
            return super.visitForLoop(node, compilationUnitSpec);
        }

        @Override
        public Void visitIf(IfTree node, CompilationUnitSpec compilationUnitSpec) {
            return super.visitIf(node, compilationUnitSpec);
        }

    }

}
