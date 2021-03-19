package com.aukocharlie.recorder4j;

import com.aukocharlie.recorder4j.annotation.Target;

/**
 * @author auko
 * @date 2021/3/15 17:17
 */
public class SimpleCase {

    private static int y = 0;

    @Target
    public static void main(String[] args) {
        int i = 1;
        while (i < 20 && y < 20) {
            i++;
            y++;
        }
    }
}
