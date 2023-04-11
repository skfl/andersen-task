package com.andersentask.bookshop.request.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

//toDo: to delete (discuss). Specification has no request status
@AllArgsConstructor
@Getter
public enum RequestStatus {
    IN_PROCESS("IN_PROCESS"),
    TO_ORDER("TO_ORDER"),
    CANCELED("CANCELED");

    private final String value;
}


