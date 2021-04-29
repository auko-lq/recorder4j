package com.aukocharlie.recorder4j;

/**
 * @author auko
 */
public class LoopCase {
    public static void main(String[] args) {
        for (int i = 1, j = 1; i < 10; i++, j++) {
            System.out.println(i);
        }

        int i = 0;
        for (i = 1; i < 10; i++) {
            System.out.println(i);
        }
    }
}
