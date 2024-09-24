package org.ecommerce.exceptions;

public class OutOfStock extends Exception {
    public OutOfStock(String message) {
        super(message);
    }
    public OutOfStock(String message, Throwable cause) {
        super(message, cause);
    }
}
