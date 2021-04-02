package com.aukocharlie.recorder4j;

import org.junit.Test;

/**
 * @author auko
 * @date 2021/3/15 22:46
 */
public class TestBuildRecorderByBuilder {
    @Test
    public void test() throws NoSuchFieldException {
        Recorder recorder = Recorder.builder()
                .addRecordClass(SimpleCase.class)
                .addRecordField(SimpleCase.class.getDeclaredField("y"))
                .main(SimpleCase.class)
                .outPutReplace("com.aukocharlie.recorder4j.", "")
                .outPutReplace("java.lang.String", "String").build();
        recorder.run();
    }

    @Test
    public void testTwoClasses() {
        Recorder recorder = Recorder.builder().main(TwoClassesCase.class).build();
        recorder.run();
    }

    @Test
    public void testThreadCase() throws InterruptedException {
        Recorder recorder = Recorder.builder().main(ThreadCase.class).mainArgs(new String[]{"123"}).build();
        recorder.run();
    }

}
