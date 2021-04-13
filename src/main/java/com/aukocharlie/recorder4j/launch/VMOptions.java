package com.aukocharlie.recorder4j.launch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author auko
 * @date 2021/3/16 15:58
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
