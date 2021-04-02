package com.aukocharlie.recorder4j.result;

import com.sun.jdi.Location;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author auko
 * @date 2021/3/29 15:45
 */
public class OutputManager {

    // Output filtering, replacePattern and replacement ont-to-one correspondence
    private static final List<Pattern> replacePatterns = new ArrayList<>();
    private static final List<String> replacements = new ArrayList<>();

    // TODO: print to file
    private PrintStream writer = System.out;

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

    public void addReplacePattern(Pattern pattern, String replacement) {
        replacePatterns.add(pattern);
        replacements.add(replacement);
    }

    public void print(String str) {
        writer.print(stringReplace(str));
    }

    public void printf(String format, Object... args) {
        writer.print(stringReplace(String.format(format, args)));
    }

    private String stringReplace(String str) {
        for (int i = 0; i < replacePatterns.size(); i++) {
            Pattern pattern = replacePatterns.get(i);
            Matcher matcher = pattern.matcher(str);
            str = matcher.replaceAll(replacements.get(i));
        }
        return str;
    }

}
