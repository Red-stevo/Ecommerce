package org.codiz.onshop.ControllerAdvice.custom;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(String message) {
        super(message);
    }
}
