package com.aukocharlie.recorder4j;

//import com.aukocharlie.recorder4j.exception.RecorderRuntimeException;
//import org.junit.Test;

import com.aukocharlie.recorder4j.exception.RecorderRuntimeException;

import java.io.*;

/**
 * @author auko
 */
public class TryCatchCase {
    public static void main(String[] args) {

        try (OutputStream one = new FileOutputStream(new File(""));
             OutputStream another = new FileOutputStream(new File(""))){
            System.out.println("test");
        } catch (Exception e) {
            System.out.println("exception");
            throw new RecorderRuntimeException("");
        }
    }
}
