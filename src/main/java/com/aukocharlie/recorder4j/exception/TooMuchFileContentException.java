package com.aukocharlie.recorder4j.exception;

/**
 * @author auko
 * @date 2021/4/8 17:27
 */
public class TooMuchFileContentException extends RuntimeException {

    public TooMuchFileContentException() {
        super();
    }

    public TooMuchFileContentException(String message) {
        super(message);
    }

}
