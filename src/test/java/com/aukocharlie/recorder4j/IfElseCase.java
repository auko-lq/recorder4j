package com.aukocharlie.recorder4j;

/**
 * @author auko
 */
public class IfElseCase {
    static {
        test("static block");
    }

    public static String tmp = test("static");


    public static void main(String[] args) {
        System.out.println(condition() ? test("true") : test("false"));

        String temp = test("local");
        if (condition()) {
            test(1);
        } else {
            test(0);
        }
    }

    public static boolean condition() {
        return true;
    }

    public static void test(int i) {
        System.out.println(i);
    }

    public static String test(String i) {
        return i;
    }
}
