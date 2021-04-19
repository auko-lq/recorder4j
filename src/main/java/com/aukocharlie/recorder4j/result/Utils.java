package com.aukocharlie.recorder4j.result;

import com.aukocharlie.recorder4j.exception.RecorderRuntimeException;
import com.sun.jdi.event.ClassPrepareEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author auko
 * @date 2021/4/18 12:21
 */
public class Utils {


    public static final Pattern LAMBDA_CLASS_NAME_PATTERN = Pattern.compile("\\$\\$Lambda\\$[0-9]+[.]+[0-9]+$");

    public static boolean isLambda(ClassPrepareEvent event) {
        return isSynthetic(event.referenceType().modifiers()) && isLambdaClassName(event.referenceType().name());
    }

    public static boolean isLambdaClassName(String className) {
        Matcher matcher = LAMBDA_CLASS_NAME_PATTERN.matcher(className);
        return matcher.find();
    }

    /**
     * example:
     * <p>
     * com.aukocharlie.recorder4j.LambdaCase$$Lambda$1.1286084959 -> com.aukocharlie.recorder4j.LambdaCase
     */
    public static String truncateLambdaClassName(String lambdaClassName) {
        Matcher matcher = LAMBDA_CLASS_NAME_PATTERN.matcher(lambdaClassName);
        if (!matcher.find()) {
            throw new RecorderRuntimeException("Not valid lambda class name");
        }

        return lambdaClassName.substring(0, matcher.start());
    }

    /**
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.1">https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.1</a>
     */
    public static boolean isSynthetic(int modifiers) {
        // ACC_SYNTHETIC 0x1000: Declared synthetic; not present in the source code.
        return (modifiers & 4096) != 0;
    }

}
