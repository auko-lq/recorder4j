package com.aukocharlie.recorder4j;

import java.util.function.Consumer;

/**
 * @author auko
 */
class LambdaCase {
    public static void main(String[] args) {
        test((arg) -> {
            print(arg);
        }, "str");

        test((arg) -> {
            print(arg);
        }, "str");
    }

    public static void test(Consumer<String> consumer, String str) {
        consumer.accept(str);
    }


    public static void print(String str) {
        System.out.println(str);
    }
}
