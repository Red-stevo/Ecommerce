package org.codiz.onshop.ControllerAdvice.custom;

public class InvalidEmailFormatException extends RuntimeException {
    public InvalidEmailFormatException(String message) {
        super(message);
    }

}
