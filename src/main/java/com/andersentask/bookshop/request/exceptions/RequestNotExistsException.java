package com.andersentask.bookshop.request.exceptions;

public class RequestNotExistsException extends RuntimeException {

    public RequestNotExistsException(String message) {
        super(message);
    }
}
