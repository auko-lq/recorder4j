package com.aukocharlie.recorder4j.result;

import com.sun.jdi.ClassNotLoadedException;
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

    // Output filtering, replacePattern and replacement one-to-one correspondence
    private static final List<Pattern> replacePatterns = new ArrayList<>();
    private static final List<String> replacements = new ArrayList<>();

    // TODO: print to file
    private PrintStream writer = System.out;


    public void addReplacePattern(Pattern pattern, String replacement) {
        replacePatterns.add(pattern);
        replacements.add(replacement);
    }

    public void print(String str) {
        writer.print(replace(str));
    }

    public void printf(String format, Object... args) {
        writer.print(replace(String.format(format, args)));
    }

    private String replace(String str) {
        for (int i = 0; i < replacePatterns.size(); i++) {
            Pattern pattern = replacePatterns.get(i);
            Matcher matcher = pattern.matcher(str);
            str = matcher.replaceAll(replacements.get(i));
        }
        return str;
    }

}
