package com.heydari.customer.exception;

public class CustomerInternalException extends  Exception{
    public CustomerInternalException() {
        super();
    }
    public CustomerInternalException(String message) {
        super(message);
    }

    public CustomerInternalException (String message, Throwable cause) {
        super(message, cause);
    }
}
