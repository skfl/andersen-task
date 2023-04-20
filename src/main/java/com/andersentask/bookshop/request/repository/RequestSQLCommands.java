package com.andersentask.bookshop.request.repository;

import com.andersentask.bookshop.book.services.BookService;

import javax.sql.DataSource;

class RequestSQLCommands {

    static final String SQL_COUNT_BY_BOOK_ID = "select count(*) as count from requests where book_id =?";
    static final String SQL_INSERT = "insert into requests(user_id, book_id) values (?, ?)";
    static final String SQL_SELECT_BY_ID = "select * from requests where id = ?";
    static final String SQL_SELECT_ALL = "select * from requests";
    static final String SQL_DELETE_BY_ID = "delete from requests where book_id = ?";


}
