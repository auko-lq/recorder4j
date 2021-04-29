package com.aukocharlie.recorder4j;

import java.util.function.Consumer;

/**
 * @author auko
 */
class LambdaCase {

    static {
        test((arg) -> {
            print(arg);
        }, "static");
    }

    public static String t = true ? test((a) -> {
        System.out.println("a");
    }) : test((a) -> {
        System.out.println("b");
    });

    public static void main(String[] args) {
        new Thread(LambdaCase::lambda2).start();
        new Thread(() -> lambda1()).start();
        Inner inner = new Inner();
    }

    public static void test(Consumer<String> consumer, String str) {
        consumer.accept(str);
    }

    public static String test(Consumer<String> consumer) {
        consumer.accept("test");
        return "test";
    }

    public static void lambda1() {
        test((arg) -> {
            print(arg);
        }, "lambda1");
    }

    public static void lambda2() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        test((arg) -> {
            print(arg);
        }, "lambda2");
//        lambda3();
    }

    public static void lambda3() {
        test((arg) -> {
            print(arg);
        }, "lambda3");
    }

    public static void print(String str) {
        System.out.println(str);
    }

    static class Inner {
        static {
            test((arg) -> {
                print(arg);
            }, "innerClass static");
        }
    }
}
