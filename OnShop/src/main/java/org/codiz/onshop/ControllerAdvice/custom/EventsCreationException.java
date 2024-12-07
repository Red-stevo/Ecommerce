package org.codiz.onshop.ControllerAdvice.custom;

public class EventsCreationException extends RuntimeException {
    public EventsCreationException(String message) {
        super(message);
    }
}
