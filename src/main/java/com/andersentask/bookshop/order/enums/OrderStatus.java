package com.andersentask.bookshop.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OrderStatus {
    IN_PROCESS("IN_PROCESS"),
    COMPLETED("COMPLETED");
    private String value;

    public int getOrdinalOfOrderEnum(){
        return this.ordinal();
    }
}
