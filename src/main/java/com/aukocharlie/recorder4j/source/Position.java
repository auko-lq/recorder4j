package com.aukocharlie.recorder4j.source;

import com.aukocharlie.recorder4j.exception.TooMuchFileContentException;

/**
 * @author auko
 */
public class Position {

    int position;

    long line;
    long col;

    private static final Position UNKNOWN_POSITION = new Position(-1, -1, -1);

    public Position(long line, long col, long position) {
        this.line = line;
        this.col = col;

        if (position > Integer.MAX_VALUE) {
            throw new TooMuchFileContentException(String.format("There is a java source file whose content contains more than %d characters", Integer.MAX_VALUE));
        }
        this.position = (int) position;
    }

    public int getPosition() {
        return this.position;
    }

    public static Position unknownPosition() {
        return UNKNOWN_POSITION;
    }

    public boolean behind(Position position) {
        return position.line < this.line ||
                (position.line == this.line && position.col < this.col);
    }

    public boolean isUnknown() {
        return this == UNKNOWN_POSITION;
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
