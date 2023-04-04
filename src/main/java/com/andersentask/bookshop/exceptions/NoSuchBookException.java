package com.andersentask.bookshop.exceptions;


public class NoSuchBookException extends RuntimeException {
    private final static String DEFAULT_MESSAGE = "No such book was found";

    public NoSuchBookException(){
        super(DEFAULT_MESSAGE);
    }

    public NoSuchBookException(String message){
        super(message);
    }
}
