package org.codiz.onshop.ControllerAdvice.custom;

public class UserDoesNotExistException extends RuntimeException {
    public UserDoesNotExistException(String message) {
        super(message);
    }
}
