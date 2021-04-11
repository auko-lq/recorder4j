package com.aukocharlie.recorder4j.source;

import com.aukocharlie.recorder4j.exception.RecorderRuntimeException;
import com.sun.source.tree.*;
import com.sun.source.util.TreeScanner;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author auko
 * @date 2021/4/7 11:50
 */
public class SourceScanner extends TreeScanner<Void, Void> {

    CompilationUnitTree compilationUnitTree = null;
    String sourceCode = null;
    LineMap lineMap = null;

    /**
     * key: methodInvocation's startPosition, value: sourcePosition.
     * <p>
     * Multiple method calls in a method chaining will have the same startPosition.
     */
    Map<SourcePosition.Position, MethodInvocationPosition> methodInvocationPositionMap = new LinkedHashMap<>();

    /**
     * example: call(paramMethod()).
     * key: paramMethod's start position
     * value: call's method invocation position
     */
    Map<SourcePosition.Position, MethodInvocationPosition> methodInParamToCallerMap = new LinkedHashMap<>();

    @Override
    public Void visitCompilationUnit(CompilationUnitTree node, Void v) {
        compilationUnitTree = node;
        try {
            sourceCode = node.getSourceFile().getCharContent(true).toString();
        } catch (IOException e) {
            throw new RecorderRuntimeException(e.getMessage());
        }
        lineMap = node.getLineMap();
        return super.visitCompilationUnit(node, v);
    }

    /**
     * Same as method invocation procession.
     */
    @Override
    public Void visitNewClass(NewClassTree node, Void v) {
        parseMethodInvocationPosition(node);
        return super.visitNewClass(node, v);
    }

    @Override
    public Void visitMethodInvocation(MethodInvocationTree node, Void v) {
        parseMethodInvocationPosition(node);
        return super.visitMethodInvocation(node, v);
    }

    private void parseMethodInvocationPosition(Tree node) {
        SourcePosition.Position startPosition = SourcePosition.getStartPosition(compilationUnitTree, node, lineMap);
        if (methodInParamToCallerMap.containsKey(startPosition)) {
            // this node is in the parameter list
            List<MethodInvocationPosition> paramMethods = methodInParamToCallerMap.get(startPosition).getParamMethodPosition();
            boolean positionMatched = false;
            for (int i = 0; i < paramMethods.size(); i++) {
                if (paramMethods.get(i).startPosition.equals(startPosition)) {
                    MethodInvocationPosition newPosition = getMethodInvocationPositionWithParam(node);
                    if (paramMethods.get(i).endPosition.behind(newPosition.endPosition)) {
                        // Linking method chaining
                        newPosition.setNextMethod(paramMethods.get(i));
                    }
                    paramMethods.set(i, newPosition);
                    positionMatched = true;
                    break;
                }
            }
            if (!positionMatched) {
                throw new RecorderRuntimeException("No matching position was found in the parameter list");
            }
        } else {
            MethodInvocationPosition newPosition = getMethodInvocationPositionWithParam(node);
            // Linking method chaining
            newPosition.setNextMethod(methodInvocationPositionMap.get(startPosition));
            methodInvocationPositionMap.put(startPosition, newPosition);
        }
    }


    /**
     * Recursively get the method invocation position in the parameter list
     *
     * @return
     */
    private MethodInvocationPosition getMethodInvocationPositionWithParam(Tree node) {
        MethodInvocationPosition res = new MethodInvocationPosition(SourcePosition.getSourcePosition(compilationUnitTree, node, lineMap));
        if (node instanceof NewClassTree) {
            for (ExpressionTree arg : ((NewClassTree) node).getArguments()) {
                if (arg.getKind() == Tree.Kind.METHOD_INVOCATION || arg.getKind() == Tree.Kind.NEW_CLASS) {
                    res.addParameterMethodPosition(this.getMethodInvocationPositionWithParam(arg));
                    methodInParamToCallerMap.put(SourcePosition.getStartPosition(compilationUnitTree, arg, lineMap), res);
                }
            }
        } else if (node instanceof MethodInvocationTree) {
            for (ExpressionTree arg : ((MethodInvocationTree) node).getArguments()) {
                if (arg.getKind() == Tree.Kind.METHOD_INVOCATION || arg.getKind() == Tree.Kind.NEW_CLASS) {
                    res.addParameterMethodPosition(this.getMethodInvocationPositionWithParam(arg));
                    methodInParamToCallerMap.put(SourcePosition.getStartPosition(compilationUnitTree, arg, lineMap), res);
                }
            }
        } else {
            throw new RecorderRuntimeException("Unknown tree node type: " + node.getKind());
        }

        return res;
    }


    public List<MethodInvocationPosition> generateMethodExecChain() {
        return methodInvocationPositionMap.values().stream()
                .map((position -> adjustPositionCol(position, position.startPosition)))
                .collect(Collectors.toList());
    }

    /**
     * Adjust the methodInvocationPosition's column for displaying better.
     * <p>
     * example: chainCase.chain("1").chain("2") -> chain("2")
     */
    private MethodInvocationPosition adjustPositionCol(MethodInvocationPosition methodInvocation, SourcePosition.Position newStartPosition) {
        if (methodInvocation == null) {
            return null;
        }
        methodInvocation.startPosition = newStartPosition;
        methodInvocation.source = sourceCode.substring(newStartPosition.position, methodInvocation.endPosition.position);

        for (MethodInvocationPosition paramMethod : methodInvocation.getParamMethodPosition()) {
            adjustPositionCol(paramMethod, paramMethod.startPosition);
        }

        char[] sourceCodeChars = sourceCode.toCharArray();
        if (methodInvocation.nextMethod() != null) {
            for (int i = methodInvocation.endPosition.position; i < sourceCodeChars.length; i++) {
                if (sourceCodeChars[i] == '.') {
                    adjustPositionCol(methodInvocation.nextMethod(),
                            new SourcePosition.Position(lineMap.getLineNumber(i + 1), lineMap.getColumnNumber(i + 1), i + 1));
                    break;
                }
            }
        }

        return methodInvocation;
    }
}
