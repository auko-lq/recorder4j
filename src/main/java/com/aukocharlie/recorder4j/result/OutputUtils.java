package com.aukocharlie.recorder4j.result;

import com.sun.jdi.Location;

/**
 * @author auko
 * @date 2021/3/29 15:45
 */
public class OutputUtils {

    static String locationToString(Location location) {
        return String.format("%s.%s(), line=%d index=%d",
                location.declaringType().name(),
                location.method().name(),
                location.lineNumber(),
                location.codeIndex());
    }

    static String locationToSimplifiedString(Location location) {
        return String.format("line=%d index=%d",
                location.lineNumber(),
                location.codeIndex());
    }

}
