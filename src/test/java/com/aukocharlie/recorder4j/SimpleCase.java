package com.aukocharlie.recorder4j;

import com.aukocharlie.recorder4j.annotation.Target;

/**
 * @author auko
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
