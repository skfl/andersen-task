package com.andersentask.bookshop.order.repositories;

public class OrderBooksSQLCommands {

    static String SQL_INSERT = "insert into order_books(order_id, book_id) " +
            "values (?, ?))";

}
