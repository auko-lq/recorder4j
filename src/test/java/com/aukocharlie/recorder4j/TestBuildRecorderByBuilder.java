package com.aukocharlie.recorder4j;

import org.junit.Test;

/**
 * @author auko
 * @date 2021/3/15 22:46
 */
public class TestBuildRecorderByBuilder {
    @Test
    public void test() throws NoSuchFieldException {
        Recorder recorder = Recorder.builder().addRecordClass(SimpleCase.class).addRecordField(SimpleCase.class.getDeclaredField("y")).main(SimpleCase.class).build();
        recorder.run();
    }

}
