package com.aukocharlie.recorder4j;

/**
 * @author auko
 */
public class ChainCase {

    public String str;

    public ChainCase(String str) {
        this.str = str;
    }

    public static void main(String[] args) {
        ChainCase chainCase = new ChainCase("0")
                .chain("2")
                .chain(new ChainCase("3").test(), new ChainCase("4").test())
                .chain("5");
        System.out.println(chainCase.str);
    }

    public ChainCase chain(String str) {
        this.str += str;
        return this;
    }

    public ChainCase chain(String str1, String str2) {
        this.str += str1 + str2;
        return this;
    }

    public String test() {
        return str + "test";
    }
}
