package com.andersentask.bookshop.order.repositories;

class OrderBooksSQLCommands {

    static String SQL_COUNT_BY_ID = "select count(order_id) as count" +
            " from order_books where order_id = ?";

    static String SQL_INSERT = "insert into order_books(order_id, book_id) " +
            "values (?, ?)";

    private OrderBooksSQLCommands() {
    }
}
