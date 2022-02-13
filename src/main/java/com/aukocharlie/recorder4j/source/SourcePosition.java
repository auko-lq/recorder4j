package com.aukocharlie.recorder4j.source;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.LineMap;
import com.sun.source.tree.Tree;
import com.sun.tools.javac.tree.EndPosTable;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeInfo;

/**
 * @author auko
 */
public class SourcePosition {

    public String source;

    public Position startPosition;
    public Position endPosition;

    public SourcePosition(Position startPosition, Position endPosition, String source) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.source = source;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public static SourcePosition getSourcePosition(CompilationUnitTree unitTree, Tree node, LineMap lineMap) {
        return new SourcePosition(getStartPosition(unitTree, node, lineMap), getEndPosition(unitTree, node, lineMap), node.toString());
    }

    public static Position getStartPosition(CompilationUnitTree unitTree, Tree node, LineMap lineMap) {
        long position = TreeInfo.getStartPos((JCTree) node);
        return new Position(lineMap.getLineNumber(position), lineMap.getColumnNumber(position), position);
    }

    public static Position getEndPosition(CompilationUnitTree unitTree, Tree node, LineMap lineMap) {
        EndPosTable endPosTable = ((JCTree.JCCompilationUnit) unitTree).endPositions;
        long position = TreeInfo.getEndPos((JCTree) node, endPosTable);
        return new Position(lineMap.getLineNumber(position), lineMap.getColumnNumber(position), position);
    }

    public static SourcePosition unknownPosition(String source) {
        return new SourcePosition(Position.unknownPosition(), Position.unknownPosition(), source);
    }

    @Override
    public String toString() {
        return String.format("%s -> %s", startPosition.toString(), endPosition.toString());
    }


}
