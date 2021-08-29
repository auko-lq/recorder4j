package com.aukocharlie.recorder4j.source.spec.block;

import com.aukocharlie.recorder4j.source.spec.CompilationUnitSpec;
import com.aukocharlie.recorder4j.source.spec.expression.MethodInvocationExpressionSpec;
import com.aukocharlie.recorder4j.source.spec.statement.BreakStatementSpec;
import com.aukocharlie.recorder4j.source.spec.statement.ContinueStatementSpec;
import com.aukocharlie.recorder4j.source.spec.statement.LabeledStatementSpec;
import com.aukocharlie.recorder4j.source.spec.statement.ReturnStatementSpec;
import com.aukocharlie.recorder4j.source.spec.statement.loop.DoWhileLoopSpec;
import com.aukocharlie.recorder4j.source.spec.statement.loop.ForLoopSpec;
import com.aukocharlie.recorder4j.source.spec.statement.loop.WhileLoopSpec;
import com.sun.source.tree.*;

/**
 * @author auko
 */
public class MethodBlockSpec extends BlockSpec {

    public MethodBlockSpec(BlockTree node, CompilationUnitSpec compilationUnitSpec, String methodName) {
        super(node, compilationUnitSpec, methodName);
    }

    @Override
    protected StatementScanner getScanner() {
        return new MethodStatementScanner(this);
    }

    @Override
    public MethodInvocationExpressionSpec nextMethodInvocation() {
        return super.nextMethodInvocation();
    }

    class MethodStatementScanner extends StatementScanner {

        public MethodStatementScanner(MethodBlockSpec methodBlockSpec) {
            super(methodBlockSpec);
        }

    }

}
