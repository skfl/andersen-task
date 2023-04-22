package com.andersentask.bookshop.book.repositories;

public class BookJPQLQueries {

    static final String SELECT_ALL = "select b from Book b";

    static final String SELECT_BY_ID = "select b from Book b where id = :id";

    static final String SELECT_SORTED_BY_PRICE = "select b from Book b order by b.price";

    static final String SELECT_SORTED_BY_ID = "select b from Book b order by b.id";

    static final String SELECT_SORTED_BY_STATUS = "select b from Book b order by b.status";

    static final String SELECT_SORTED_BY_NAME = "select b from Book b order by b.name";
}
