package com.aukocharlie.recorder4j;

//import com.aukocharlie.recorder4j.exception.RecorderRuntimeException;
//import org.junit.Test;

import com.aukocharlie.recorder4j.exception.RecorderRuntimeException;

/**
 * @author auko
 */
public class TryCatchCase {
    public static void main(String[] args) {
        try {
            System.out.println("test");
//            Method method  = TryCatchCase.class.getMethod("main", String[].class).
        } catch (Exception e) {
            System.out.println("exception");
            throw new RecorderRuntimeException("");
        }
    }
}
