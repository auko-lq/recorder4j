package com.aukocharlie.recorder4j;

/**
 * @author auko
 */
public class BinaryCase {
    public static void main(String[] args) {
        System.out.println(condition() ? leftOperand() : rightOperand());
        System.out.println(leftOperand() >> rightOperand());
        System.out.println(leftOperand() * (leftOperand() + rightOperand()));
        System.out.println(leftOperand() == 1 || rightOperand() == 2);
    }

    public static boolean condition() {
        return true;
    }

    public static int leftOperand() {
        return 1;
    }

    public static int rightOperand() {
        return 2;
    }
}
