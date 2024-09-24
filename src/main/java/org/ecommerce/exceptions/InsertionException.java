package org.ecommerce.exceptions;

public class InsertionException extends RuntimeException {
    public InsertionException(String message) {
        super(message);
    }
    public InsertionException(String message, Throwable cause) {
        super(message, cause);
    }
}
