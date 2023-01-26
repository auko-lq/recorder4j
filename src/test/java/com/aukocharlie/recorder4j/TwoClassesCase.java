package com.aukocharlie.recorder4j;


/**
 * @author auko
 */
public class TwoClassesCase {

    public static int y;
    public int z;

    public static void main(String[] args) {
        TwoClassesCase twoClassesCase = new TwoClassesCase();

        class InnerClass extends OneClass {

        }

        InnerClass one = new InnerClass();
        InnerClass two = new InnerClass();
        AnotherClass another = new AnotherClass();

        System.out.println(twoClassesCase.test(0, one, two, another));
    }

    public String test(int i, OneClass oneClass, OneClass twoClass, AnotherClass anotherClass) {
        while (i < 10) {
            i++;
            twoClass.j++;
            TwoClassesCase.y++;
            anotherClass.k++;
            z++;
        }
        String str = "res: ";
        return str + (i + oneClass.j + twoClass.j + TwoClassesCase.y + anotherClass.k + z);
    }

}

class OneClass {
    public int j = 0;
}

class AnotherClass {

    public int k;

    public static void test(){
        System.out.println("test");
    }
}
