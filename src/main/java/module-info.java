module recorder4j.main {
    requires java.compiler;
    requires jdk.jdi;
    requires jdk.compiler;
    requires static lombok;
    requires java.management;
    exports com.aukocharlie.recorder4j.annotation;
    exports com.aukocharlie.recorder4j.constant;
    exports com.aukocharlie.recorder4j.exception;
    exports com.aukocharlie.recorder4j.source;
    exports com.aukocharlie.recorder4j;
}