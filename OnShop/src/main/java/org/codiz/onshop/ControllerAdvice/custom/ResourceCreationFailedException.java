package org.codiz.onshop.ControllerAdvice.custom;

public class ResourceCreationFailedException extends RuntimeException {
    public ResourceCreationFailedException(String message) {
        super(message);
    }
}
