package com.aukocharlie.recorder4j.source.spec;

import com.aukocharlie.recorder4j.source.MethodInvocationPosition;
import com.sun.source.tree.ExpressionTree;

import java.util.ArrayList;
import java.util.List;

/**
 * @author auko
 */
public class ExpressionSpec implements Expression {

    public ExpressionSpec(ExpressionTree node, CompilationUnitSpec compilationUnitSpec) {

    }

    @Override
    public List<MethodInvocationPosition> getMethodInvocations() {
        return null;
    }

    @Override
    public List<BlockSpec> getLambdaBlockList() {
        return new ArrayList<>();
    }

//    List<BlockSpec> lambdaBlocks = new ArrayList<>();
//
//    public ExpressionSpec(ExpressionTree node){
//
//    }
//
//    @Override
//    public List<MethodInvocationPosition> getMethodInvocations() {
//        return null;
//    }
//
//    @Override
//    public List<BlockSpec> getLambdaList() {
//        return null;
//    }
//
//
//    class ExpressionScanenr extends TreeScanner<Void, Void>{
//
//        /**
//         * key: methodInvocation's startPosition, value: sourcePosition.
//         * <p>
//         * Multiple method calls in a method chaining will have the same startPosition.
//         */
//        Map<Position, MethodInvocationPosition> methodInvocationPositionMap = new LinkedHashMap<>();
//
//        /**
//         * example: call(argMethod()).
//         * <p>
//         * key: argMethod's start position
//         * <p>
//         * value: call's method invocation position
//         */
//        Map<Position, MethodInvocationPosition> methodInArgToCallerMap = new LinkedHashMap<>();
//
//
//        /**
//         * Same as method invocation procession.
//         */
//        @Override
//        public Void visitNewClass(NewClassTree node, Void v) {
//            parseMethodInvocationPosition(node);
//            return super.visitNewClass(node, v);
//        }
//
//        @Override
//        public Void visitMethodInvocation(MethodInvocationTree node, Void v) {
//            parseMethodInvocationPosition(node);
//            return super.visitMethodInvocation(node, v);
//        }
//
//
//        private void parseMethodInvocationPosition(Tree node) {
////        System.out.println(node.getClass());
//            Position startPosition = getSourceScanner().getStartPosition(node);
//            if (methodInArgToCallerMap.containsKey(startPosition)) {
//                // this node is in the argument list
//                List<MethodInvocationPosition> argMethods = methodInArgToCallerMap.get(startPosition).getArgMethodPosition();
//                boolean positionMatched = false;
//                for (int i = 0; i < argMethods.size(); i++) {
//                    // All method invocation on the same method chaining will have the same startPosition
//                    if (argMethods.get(i).startPosition.equals(startPosition)) {
//                        MethodInvocationPosition newPosition = getMethodInvocationPositionWithArg(node);
//                        if (argMethods.get(i).endPosition.behind(newPosition.endPosition)) {
//                            // Linking method chaining
//                            newPosition.setNextMethod(argMethods.get(i));
//                        }
//                        argMethods.set(i, newPosition);
//                        positionMatched = true;
//                        break;
//                    }
//                }
//                if (!positionMatched) {
//                    throw new RecorderRuntimeException("No matching position was found in the argument list");
//                }
//            } else {
//                MethodInvocationPosition newPosition = getMethodInvocationPositionWithArg(node);
//                // Linking method chaining
//                newPosition.setNextMethod(methodInvocationPositionMap.get(startPosition));
//                methodInvocationPositionMap.put(startPosition, newPosition);
//            }
//        }
//
//
//        /**
//         * Recursively get the method invocation position in the argument list
//         */
//        private MethodInvocationPosition getMethodInvocationPositionWithArg(Tree node) {
////        System.out.println(node);
//            MethodInvocationPosition res = new MethodInvocationPosition(getSourceScanner().getSourcePosition(node));
//            if (node instanceof NewClassTree) {
//                for (ExpressionTree arg : ((NewClassTree) node).getArguments()) {
//                    if (arg.getKind() == Tree.Kind.METHOD_INVOCATION || arg.getKind() == Tree.Kind.NEW_CLASS) {
//                        res.addArgMethodPosition(this.getMethodInvocationPositionWithArg(arg));
//                        methodInArgToCallerMap.put(getSourceScanner().getStartPosition(arg), res);
//                    }
//                }
//            } else if (node instanceof MethodInvocationTree) {
//                for (ExpressionTree arg : ((MethodInvocationTree) node).getArguments()) {
////                System.out.println(arg.getKind());
//                    if (arg.getKind() == Tree.Kind.METHOD_INVOCATION || arg.getKind() == Tree.Kind.NEW_CLASS) {
//                        res.addArgMethodPosition(this.getMethodInvocationPositionWithArg(arg));
//                        methodInArgToCallerMap.put(getSourceScanner().getStartPosition(arg), res);
//                    } else if (arg.getKind() == Tree.Kind.LAMBDA_EXPRESSION) {
//                        System.out.println("!!!!!! lambda position: " + getSourceScanner().getSourcePosition(arg));
//                    }
//                }
//            } else {
//                throw new RecorderRuntimeException("Unknown tree node type: " + node.getKind());
//            }
//
//            return res;
//        }
//
//    }
}
