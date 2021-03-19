package com.aukocharlie.recorder4j;

import org.junit.Test;

/**
 * @author auko
 * @date 2021/3/15 22:46
 */

public class TestBuildRecorderByAnnotation {

    @Test
    public void test() {
        Recorder recorder = Recorder.builder().scanPackage("com.aukocharlie.recorder4j").build();
    }
}
