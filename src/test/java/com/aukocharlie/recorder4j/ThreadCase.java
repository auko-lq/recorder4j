package com.aukocharlie.recorder4j;

/**
 * @author auko
 * @date 2021/3/20 21:35
 */
public class ThreadCase {

    static int k = 50;

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            int auko = 456;
            try {
                System.out.println(getStr(auko));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "test-thread");
        thread.start();
        thread.join();
    }

    public static String getStr(int i) throws InterruptedException {
        String res = "res: ";
        int j = i + 10;
        k = 20;
        j = j + k;
        return res + j;
    }


}
