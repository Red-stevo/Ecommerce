package org.codiz.onshop.ControllerAdvice.custom;

public class UserAlreadyExistException extends RuntimeException {
    public UserAlreadyExistException(String message) {
        super(message);
    }
}
