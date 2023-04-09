package com.andersentask.bookshop.request.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RequestStatus {
    SENT, IN_PROCESSING, TO_ORDER
}
