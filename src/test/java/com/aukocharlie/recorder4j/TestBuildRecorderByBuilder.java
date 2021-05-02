package com.aukocharlie.recorder4j;

import org.junit.Test;

/**
 * @author auko
 */
public class TestBuildRecorderByBuilder {
    @Test
    public void testSimpleCase() throws NoSuchFieldException {
        Recorder recorder = Recorder.builder()
                .addRecordClass(SimpleCase.class)
                .addRecordField(SimpleCase.class.getDeclaredField("y"))
                .main(SimpleCase.class)
                .srcRelativeRootPath("src/test/java")
                .outPutReplace("com.aukocharlie.recorder4j.", "")
                .outPutReplace("java.lang.String", "String").build();
        recorder.run();
    }


    @Test
    public void testChainCase() {
        Recorder recorder = Recorder.builder()
                .main(ChainCase.class)
                .srcRelativeRootPath("src/test/java")
                .outPutReplace("com.aukocharlie.recorder4j.", "")
                .build();
        recorder.run();
    }

    @Test
    public void testTwoClasses() {
        Recorder recorder = Recorder.builder()
                .main(TwoClassesCase.class)
                .srcRelativeRootPath("src/test/java")
                .outPutReplace("com.aukocharlie.recorder4j.", "")
                .build();
        recorder.run();
    }

    @Test
    public void testThreadCase() throws InterruptedException {
        Recorder recorder = Recorder.builder()
                .srcRelativeRootPath("src/test/java")
                .main(ThreadCase.class)
                .mainArgs(new String[]{"123"})
                .build();
        recorder.run();
    }

    @Test
    public void testLambdaCase() {
        Recorder recorder = Recorder.builder()
                .srcRelativeRootPath("src/test/java")
                .main(LambdaCase.class)
                .build();
        recorder.run();
    }

    @Test
    public void testInnerClassCase() {
        Recorder recorder = Recorder.builder()
                .outPutReplace("com.aukocharlie.recorder4j.", "")
                .srcRelativeRootPath("src/test/java")
                .main(InnerClassCase.class)
                .build();
        recorder.run();
    }


    @Test
    public void testIfElseCase() {
        Recorder recorder = Recorder.builder()
                .outPutReplace("com.aukocharlie.recorder4j.", "")
                .srcRelativeRootPath("src/test/java").main(IfElseCase.class)
                .build();
        recorder.run();
    }

    @Test
    public void testLoopCase() {
        Recorder recorder = Recorder.builder()
                .outPutReplace("com.aukocharlie.recorder4j.", "")
                .srcRelativeRootPath("src/test/java").main(LoopCase.class)
                .build();
        recorder.run();
    }

    @Test
    public void testBinaryCase() {
        Recorder recorder = Recorder.builder()
                .outPutReplace("com.aukocharlie.recorder4j.", "")
                .srcRelativeRootPath("src/test/java").main(BinaryCase.class)
                .build();
        recorder.run();
    }

    @Test
    public void testTryCatchCase() {
        Recorder recorder = Recorder.builder()
                .outPutReplace("com.aukocharlie.recorder4j.", "")
                .srcRelativeRootPath("src/test/java").main(TryCatchCase.class)
                .build();
        recorder.run();
    }

}
