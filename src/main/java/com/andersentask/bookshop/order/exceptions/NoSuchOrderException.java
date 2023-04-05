package com.andersentask.bookshop.order.exceptions;

public class NoSuchOrderException extends RuntimeException {
    private final static String DEFAULT_MESSAGE = "No such order was found";

    public NoSuchOrderException() {
        super(DEFAULT_MESSAGE);
    }

    public NoSuchOrderException(String message) {
        super(message);
    }

}
