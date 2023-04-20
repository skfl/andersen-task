package com.andersentask.bookshop.order.repositories;

public class OrderSQLCommands {

    static String SQL_SELECT_ALL = "select *" +
            " from orders " +
            "left join order_books " +
            "on orders.id = order_books.order_id";

    static String SQL_SELECT_BY_ID = SQL_SELECT_ALL +
            " where id = ?";

    static String SQL_SELECT_ALL_SORTED_COST = SQL_SELECT_ALL +
            " order by cost desc";

    static String SQL_SELECT_ALL_SORTED_STATUS = SQL_SELECT_ALL
            + " order by status";

    static String SQL_SELECT_ALL_SORTED_TIME = SQL_SELECT_ALL +
            " order by time_of_completing desc";

    static String SQL_SELECT_COST_OF_COMPLETED_WITHIN_PERIOD = "select sum(cost) as total_cost from orders " +
            "where status = 'completed' " +
            "and (time_of_completing >= ? and time_of_completing <= ?)";

    static String SQL_UPDATE = "update orders set user_id = ?, cost = ?, status = ?, time_of_completing = ?" +
            " where id = ?";

    static String SQL_INSERT = "insert into orders(user_id,cost,status,time_of_completing) " +
            "values (?, ?, ?, ?)";
}
