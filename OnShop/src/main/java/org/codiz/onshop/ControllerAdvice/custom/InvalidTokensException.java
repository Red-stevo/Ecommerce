package org.codiz.onshop.ControllerAdvice.custom;

public class InvalidTokensException extends RuntimeException{
    public InvalidTokensException(String message) {
        super(message);
    }
}
