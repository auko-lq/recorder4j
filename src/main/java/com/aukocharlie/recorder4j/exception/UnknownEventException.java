package com.aukocharlie.recorder4j.exception;


import com.sun.jdi.event.Event;

/**
 * @author auko
 */
public class UnknownEventException extends RuntimeException {

    public UnknownEventException(Event event) {
        super(event.toString() + ", request: " + event.request());
    }

}
