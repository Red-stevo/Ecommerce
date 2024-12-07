package org.codiz.onshop.ControllerAdvice.custom;

public class UsersCreationFailedException extends RuntimeException {
    public UsersCreationFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
