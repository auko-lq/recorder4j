package com.aukocharlie.recorder4j.exception;

/**
 * @author auko
 * @date 2021/3/16 16:49
 */
public class MissingLaunchingConnectorException extends RuntimeException {

    public MissingLaunchingConnectorException() {

    }

    public MissingLaunchingConnectorException(String message) {
        super(message);
    }

}
