package com.aukocharlie.recorder4j.util;

import com.aukocharlie.recorder4j.exception.RecorderRuntimeException;

/**
 * @author auko
 */
public class Assert {

    public static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new RecorderRuntimeException(message);
        }
    }

}
