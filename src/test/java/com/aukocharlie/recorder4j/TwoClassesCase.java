package com.aukocharlie.recorder4j;

import java.io.*;

/**
 * @author auko
 * @date 2021/3/19 18:06
 */
public class TwoClassesCase {

    public static int y;
    public int z;

    public static void main(String[] args) {
        TwoClassesCase twoClassesCase = new TwoClassesCase();
        OneClass one = new OneClass();
        OneClass two = new OneClass();
        AnotherClass another = new AnotherClass();

        System.out.println(twoClassesCase.test(0, one, two, another));
    }

    public String test(int i, OneClass oneClass, OneClass twoClass, AnotherClass anotherClass) {
        while (i < 10) {
            i++;
            oneClass.j[0]++;
            twoClass.j[0]++;
            TwoClassesCase.y++;
            anotherClass.k++;
            z++;
        }
        String str = "res: ";
        return str + (i + oneClass.j[0] + twoClass.j[0] + TwoClassesCase.y + anotherClass.k + z);
    }

}

class OneClass {
    public int[] j = new int[]{0};
}

class AnotherClass {

    public int k;
}
