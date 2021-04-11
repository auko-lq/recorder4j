package com.aukocharlie.recorder4j.source;

import com.aukocharlie.recorder4j.exception.TooMuchFileContentException;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.LineMap;
import com.sun.source.tree.Tree;
import com.sun.tools.javac.tree.EndPosTable;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeInfo;

/**
 * @author auko
 * @date 2021/4/7 12:08
 */
public class SourcePosition {

    String source;

    Position startPosition;
    Position endPosition;

    protected SourcePosition(Position startPosition, Position endPosition, String source) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
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


    @Override
    public String toString() {
        return String.format("%s -> %s", startPosition.toString(), endPosition.toString());
    }

    static class Position {

        int position;

        long line;
        long col;

        public Position(long line, long col, long position) {
            this.line = line;
            this.col = col;

            if (position > Integer.MAX_VALUE) {
                throw new TooMuchFileContentException(String.format("There is a java source file whose content contains more than %d characters", Integer.MAX_VALUE));
            }
            this.position = (int) position;
        }

        public boolean behind(Position position) {
            return position.line < this.line ||
                    (position.line == this.line && position.col < this.col);
        }

        @Override
        public boolean equals(Object position) {
            if (position instanceof Position) {
                return ((Position) position).line == this.line && ((Position) position).col == this.col;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return position;
        }

        @Override
        public String toString() {
            return String.format("%d:%d", line, col);
        }
    }

}
