package com.aukocharlie.recorder4j.launch;

import java.util.ArrayList;
import java.util.List;

/**
 * @author auko
 * @date 2021/3/16 15:58
 */
public class VMOptions {

    private static final List<String> defaultOptions;

    static {
        defaultOptions = new ArrayList<>();
        // -parameters
//        defaultOptions.add("-parameters");
    }

    private List<String> options = new ArrayList<>();

    public void addOption(String option) {
        options.add(option);
    }

    public String toString() {
        options.addAll(defaultOptions);
        return String.join(" ", options);
    }
}
