package com.aukocharlie.recorder4j;

/**
 * @author auko
 */
public class InnerClassCase {

    public static void main(String[] args) {
        InnerClassCase innerClassCase = new InnerClassCase();
        innerClassCase.test();
    }

    public void test() {
        InnerStaticClass innerStaticClass = new InnerStaticClass();
        innerStaticClass.test();
    }

}

class InnerStaticClass {
    class InnerClass {
        public void test() {
            System.out.println("inner class");
        }
    }

    public void test() {
        InnerStaticClass.InnerClass innerClass = new InnerStaticClass.InnerClass();
        innerClass.test();
        System.out.println("inner static class");
    }

}