package com.andersentask.bookshop.request.repository;

class RequestJPQLQueries {

    static final String SQL_SELECT_ALL = "select r from Request r";

    static final String SQL_SELECT_BY_ID = "select r from Request r where id = :id";

    static final String SQL_DELETE_BY_ID = "delete from Request r where book = :book";

    static final String SQL_COUNT_BY_BOOK_ID = "select count(r.id) from Request r" +
            " where book = :book";

    private RequestJPQLQueries() {
    }
}
