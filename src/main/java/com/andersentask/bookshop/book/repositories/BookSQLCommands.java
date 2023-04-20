package com.andersentask.bookshop.book.repositories;

public class BookSQLCommands {
    static final String SQL_SELECT_ALL = "select id, name, status, price from books";

    static final String SQL_SELECT_SORTED_ID = "select id, name, status, price from books order by id";

    static final String SQL_SELECT_SORTED_NAME = "select id, name, status, price from books order by name";

    static final String SQL_SELECT_SORTED_STATUS = "select id, name, status, price from books order by status";

    static final String SQL_SELECT_SORTED_PRICE = "select id, name, status, price from books order by price";

    static final String SQL_SELECT_BY_ID = "select id, name, status, price from books where id = ?";

    static final String SQL_INSERT = "insert into books(name, status, price) values (?, ?, ?)";

    static final String SQL_UPDATE = "update books set name = ?, status = ?, price = ? where id = ?";
}
