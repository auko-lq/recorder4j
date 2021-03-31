package com.aukocharlie.recorder4j;

import com.aukocharlie.recorder4j.annotation.Target;

/**
 * @author auko
 * @date 2021/3/15 17:17
 */
public class SimpleCase {

    private static int y;

    private int k;

    public SimpleCase() {
        y = 0;
        k = 0;
    }

    @Target
    public static void main(String[] args) {
        SimpleCase simple = new SimpleCase();
        System.out.println(simple.test(0));
    }

    public String test(int i) {
        while (i < 10) {
            i++;
            y++;
            k++;
        }
        String str = "res: ";
        return str + (i + y + k);
    }
}
