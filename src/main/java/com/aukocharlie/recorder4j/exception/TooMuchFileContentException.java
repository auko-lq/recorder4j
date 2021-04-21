package com.aukocharlie.recorder4j.exception;

/**
 * @author auko
 */
public class TooMuchFileContentException extends RuntimeException {

    public TooMuchFileContentException() {
        super();
    }

    public TooMuchFileContentException(String message) {
        super(message);
    }

}
