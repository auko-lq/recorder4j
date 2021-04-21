package com.aukocharlie.recorder4j.launch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author auko
 */
public class VMOptions {

    private static final List<String> defaultOptions = new ArrayList<>();

    private List<String> options = new ArrayList<>();

    public void addOption(String option) {
        options.add(option);
    }

    @Override
    public String toString() {
        options.addAll(defaultOptions);
        return String.join(" ", options);
    }
}
