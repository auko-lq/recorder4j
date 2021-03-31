package com.aukocharlie.recorder4j.result;

import com.sun.jdi.event.ExceptionEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author auko
 * @date 2021/3/21 13:37
 */
public class ExceptionFilter {

    private static final Set<String> exceptionExclusion = new HashSet<>();
    private static final Map<String, String> catchLocationExclusion = new HashMap<>();

    static {
        // ClassNotFoundException thrown if class not found
        // from the non-null parent class loader. That's harmless.
        catchLocationExclusion.put("java.lang.ClassLoader:436", null);
        catchLocationExclusion.put("java.lang.ClassLoader:415", null);

        // Anonymous function related, harmless.
        catchLocationExclusion.put("java.lang.invoke.MemberName$Factory:978", "java.lang.NoSuchFieldError");
        catchLocationExclusion.put("java.lang.invoke.MethodHandleImpl:1388", "java.lang.NoSuchMethodException");

    }

    public static boolean skip(ExceptionEvent event) {
        String locationMatched = catchLocationExclusion.get(event.catchLocation().toString());
        return exceptionExclusion.contains(event.exception().referenceType().name())
                || locationMatched == null
                || locationMatched.equals(event.exception().referenceType().name());
    }

}
