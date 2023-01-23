package com.aukocharlie.recorder4j.source.scanner;

import com.aukocharlie.recorder4j.source.SourceScanner;
import com.aukocharlie.recorder4j.source.spec.CompilationUnitSpec;
import com.aukocharlie.recorder4j.source.spec.expression.*;
import com.sun.source.tree.*;
import lombok.Getter;

import static com.sun.source.tree.LambdaExpressionTree.BodyKind.STATEMENT;

public class ExpressionScanner extends SourceScanner {

    @Getter
    private AbstractExpressionSpec specificExpr = null;

    private final String scannedExpr;

    public ExpressionScanner(String expr) {
        this.scannedExpr = expr;
    }

    @Override
    public Void visitLambdaExpression(LambdaExpressionTree node, CompilationUnitSpec compilationUnitSpec) {
        if (this.specificExpr == null && node.getBodyKind() == STATEMENT) {
            this.specificExpr = new LambdaExpressionSpec(node, compilationUnitSpec, scannedExpr);
        }
        return null;
    }

    @Override
    public Void visitConditionalExpression(ConditionalExpressionTree node, CompilationUnitSpec compilationUnitSpec) {
        if (this.specificExpr == null) {
            this.specificExpr = new ConditionExpressionSpec(node, compilationUnitSpec, scannedExpr);
        }
        return null;
    }

    @Override
    public Void visitBinary(BinaryTree node, CompilationUnitSpec compilationUnitSpec) {
        if (this.specificExpr == null) {
            this.specificExpr = new BinaryExpressionSpec(node, compilationUnitSpec, scannedExpr);
        }
        return null;
    }

    @Override
    public Void visitMethodInvocation(MethodInvocationTree node, CompilationUnitSpec compilationUnitSpec) {
        if (this.specificExpr == null) {
            this.specificExpr = MethodInvocationExpressionSpec.getFirstMethodInvocationOnChain(node, compilationUnitSpec, scannedExpr);
        }
        return null;
    }


    /**
     * Think of <em>new class</em> as a <em>method invocation</em> (constructor method)
     */
    @Override
    public Void visitNewClass(NewClassTree node, CompilationUnitSpec compilationUnitSpec) {
        if (this.specificExpr == null) {
            this.specificExpr = MethodInvocationExpressionSpec.getFirstMethodInvocationOnChain(node, compilationUnitSpec, scannedExpr);
        }
        return null;
    }
}
