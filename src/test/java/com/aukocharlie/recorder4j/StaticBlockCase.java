package com.aukocharlie.recorder4j;

/**
 * @author auko
 */
public class StaticBlockCase {
    static {
        System.out.println("static block");
    }

    public static void main(String[] args) {
        StaticBlockCase staticBlockCase = new StaticBlockCase();
        staticBlockCase.test();
    }

    public void test() {
        System.out.println("test");
    }
}
