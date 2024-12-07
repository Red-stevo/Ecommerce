package org.codiz.onshop.ControllerAdvice.custom;

public class EntityDeletionException  extends RuntimeException {
    public EntityDeletionException(String message) {
        super(message);
    }
}
