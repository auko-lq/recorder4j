package com.aukocharlie.recorder4j;

/**
 * @author auko
 * @date 2021/3/19 18:06
 */
public class TwoClassesCase {

    public String str = "one";

    public static void main(String[] args) {
        String str = new TwoClassesCase().str + " " + new AnotherClass().another();
        System.out.println(str);
    }

}

class AnotherClass {
    public String anotherClassStr = "another";

    public String another() {
        return this.anotherClassStr;
    }
}
