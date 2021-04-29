package com.aukocharlie.recorder4j.result;

import com.sun.jdi.event.ExceptionEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author auko
 */
public class ExceptionFilter {

    private static final Set<String> exceptionExclusion = new HashSet<>();
    private static final Map<String, String> catchLocationExclusion = new HashMap<>();
    private static final String ALL_EXCEPTION = "all";

    static {
        // ClassNotFoundException thrown if class not found
        // from the non-null parent class loader. That's harmless.
        catchLocationExclusion.put("java.lang.ClassLoader:436", ALL_EXCEPTION);
        catchLocationExclusion.put("java.lang.ClassLoader:415", ALL_EXCEPTION);

        // Anonymous function related, harmless.
        catchLocationExclusion.put("java.lang.invoke.MemberName$Factory:978", "java.lang.NoSuchFieldError");
        catchLocationExclusion.put("java.lang.invoke.MethodHandleImpl:1388", "java.lang.NoSuchMethodException");

        /* When running the program with the IDE, the IDE will add a path like
         * <em>build/resource/test</em> to the classpath, but gradle will ignore
         * the empty resource folder during the build, so the <em>resources/test</em>
         * folder will not be generated under the build folder, resulting in
         * FileNotFoundException and PrivilegedActionException indirectly.
         */
        catchLocationExclusion.put("sun.misc.URLClassPath:526", "java.io.FileNotFoundException");
        catchLocationExclusion.put("sun.misc.URLClassPath$JarLoader:901", "java.security.PrivilegedActionException");
        catchLocationExclusion.put("sun.misc.URLClassPath:569", "java.security.PrivilegedActionException");
    }

    public static boolean skip(ExceptionEvent event) {
        String locationMatched = event.catchLocation() == null ? null : catchLocationExclusion.get(event.catchLocation().toString());
        return exceptionExclusion.contains(event.exception().referenceType().name())
                || ALL_EXCEPTION.equals(locationMatched)
                || (locationMatched != null && locationMatched.equals(event.exception().referenceType().name()));
    }

}
