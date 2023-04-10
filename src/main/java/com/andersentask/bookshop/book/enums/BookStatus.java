package com.andersentask.bookshop.book.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BookStatus {
    AVAILABLE("AVAILABLE"),
    NOT_AVAILABLE("NOT_AVAILABLE"),
    OUT_OF_STOCK("OUT_OF_STOCK");


    private String value;
}
