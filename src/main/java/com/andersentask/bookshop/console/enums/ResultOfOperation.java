package com.andersentask.bookshop.console.enums;


public enum ResultOfOperation {
    ;

    public enum SetBookStatus {
        WRONG_BOOK_ID,
        BOOK_ALREADY_HAS_THIS_STATUS,
        BOOK_STATUS_UPDATED,
        INCORRECT_ENTRANCE_OF_BOOK_ID_OR_BOOK_STATUS
    }

    public enum CreateRequest {
        WRONG_BOOK_ID,
        REQUEST_CREATED,
        INCORRECT_ENTRANCE_OF_BOOK_ID
    }

    public enum CreateOrder {
        WRONG_BOOK_ID,
        ORDER_CREATED,
        ORDER_AND_REQUESTS_CREATED,
        INCORRECT_ENTRANCE_OF_BOOK_ID
    }

    public enum ChangeStatusOfOrderIncludingBooksCheck {
        WRONG_ORDER_ID,
        ORDER_STATUS_CAN_NOT_BE_UPDATED,
        STATUS_UPDATED,
        INCORRECT_ENTRANCE_OF_ORDER_ID_OR_BOOK_STATUS
    }

    public enum GetNumberOfRequestsOnBook {
        WRONG_BOOK_ID,
        INCORRECT_ENTRANCE_OF_BOOK_ID
    }

    public enum GetAllBooksFromOrder {
        WRONG_ORDER_ID,
        THE_LIST_OF_BOOKS_BELOW,
    }

    public enum getIncomeForPeriod {
        WRONG_DATE
    }

    public enum GetBooksAndNumberOfRequests {
        NO_REQUESTS
    }
}
