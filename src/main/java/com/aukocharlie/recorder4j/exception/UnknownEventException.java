package com.aukocharlie.recorder4j.exception;


import com.sun.jdi.event.Event;

/**
 * @author auko
 * @date 2021/3/20 17:57
 */
public class UnknownEventException extends Exception {

    public UnknownEventException(Event event) {
        super(event.toString() + ", request: " + event.request());
    }

}
