package com.aukocharlie.recorder4j.result;


import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author auko
 */
public class OutputManager {

    // Output filtering, replacePattern and replacement one-to-one correspondence
    private static final List<Pattern> replacePatterns = new ArrayList<>();
    private static final List<String> replacements = new ArrayList<>();

    // TODO: print to file
    private PrintStream writer = System.out;

    private boolean displayMethodPosition = false;


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

    public void setDisplayMethodPosition(boolean display) {
        this.displayMethodPosition = display;
    }

    public boolean shouldDisplayMethodPosition() {
        return this.displayMethodPosition;
    }

}
