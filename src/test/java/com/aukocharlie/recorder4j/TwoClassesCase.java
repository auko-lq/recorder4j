package com.aukocharlie.recorder4j;

/**
 * @author auko
 * @date 2021/3/19 18:06
 */
public class TwoClassesCase {

    public static int y;

    public static void main(String[] args) {
        TwoClassesCase twoClassesCase = new TwoClassesCase();
        OneClass one = new OneClass();
        AnotherClass another = new AnotherClass();

        System.out.println(twoClassesCase.test(0, one, another));
    }

    public String test(int i, OneClass oneClass, AnotherClass anotherClass) {
        while (i < 10) {
            i++;
            oneClass.j++;
            TwoClassesCase.y++;
            anotherClass.k++;
        }
        String str = "res: ";
        return str + (i + oneClass.j + TwoClassesCase.y + anotherClass.k);
    }

}

class OneClass {
    public int j;
}

class AnotherClass {

    public int k;
}
