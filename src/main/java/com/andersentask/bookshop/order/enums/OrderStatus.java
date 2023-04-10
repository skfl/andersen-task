package com.andersentask.bookshop.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OrderStatus {
    IN_PROCESS("IN_PROCESS"),
    COMPLETED("COMPLETED"),
    CANCELED("CANCELED");

    private final String value;
}
