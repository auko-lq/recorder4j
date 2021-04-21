package com.aukocharlie.recorder4j.result;

import com.sun.jdi.ClassNotLoadedException;
import com.sun.jdi.Location;
import com.sun.jdi.event.MethodEntryEvent;

/**
 * @author auko
 */
public class FormatUtils {

    static String locationToString(Location location) {
        try {
            return String.format("%s.%s(%s), line=%d",
                    location.declaringType().name(),
                    location.method().name(),
                    location.method().argumentTypes().toString(),
                    location.lineNumber());
        } catch (ClassNotLoadedException e) {
            e.printStackTrace();
        }
        return "unknown location";
    }

    static String locationToSimplifiedString(Location location) {
        return String.format("line=%d index=%d",
                location.lineNumber(),
                location.codeIndex());
    }

    static String methodEntryToString(MethodEntryEvent event){
//        return String.format("%s.%s(%s)",
//                event.method().name())
        return "";
    }

}
