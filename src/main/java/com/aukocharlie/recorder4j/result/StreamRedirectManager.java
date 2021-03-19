package com.aukocharlie.recorder4j.result;

/**
 * @author auko
 * @date 2021/3/16 13:39
 */
public class StreamRedirectManager {

    private boolean toConsole = true;

    private static final int BUFFER_SIZE = 2048;

//    private

    public void setToConsole(boolean toConsole) {
        this.toConsole = toConsole;
    }
}
