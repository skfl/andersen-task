package com.andersentask.bookshop.request.exceptions;

public class NoSuchRequestException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "No such request was found";

    public NoSuchRequestException() {
        super(DEFAULT_MESSAGE);
    }

    public NoSuchRequestException(String message) {
        super(message);
    }
}
