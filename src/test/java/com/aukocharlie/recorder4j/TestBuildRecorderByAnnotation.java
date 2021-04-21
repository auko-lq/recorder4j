package com.aukocharlie.recorder4j;

import org.junit.Test;

/**
 * @author auko
 */

public class TestBuildRecorderByAnnotation {

    @Test
    public void test() {
        Recorder recorder = Recorder.builder().scanPackage("com.aukocharlie.recorder4j").build();
    }
}
