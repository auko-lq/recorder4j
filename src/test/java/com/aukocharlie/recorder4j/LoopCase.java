package com.aukocharlie.recorder4j;

/**
 * @author auko
 * @date 2021/4/19 16:21
 */
public class LoopCase {
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println(condition() ? small() : big());
        }
    }

    public static boolean condition() {
        return true;
    }

    public static int small() {
        return 0;
    }

    public static int big() {
        return 10;
    }


}
