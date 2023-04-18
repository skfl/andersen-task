package com.andersentask.bookshop.order.repositories;

public class OrderSQLCommands {

   static String SQL_COUNT_BY_ID = "select count(order_id) as total from orders where order_id = ?";
   static String SQL_SELECT_ALL = "select (*)" +
           " from orders " +
           "left join order_books " +
           "on orders.id = order_books.order_id";


   static String SQL_SELECT_BY_ID = SQL_SELECT_ALL + " where orders.id = ?";

   static String SQL_SELECT_ALL_SORTED_COST = SQL_SELECT_ALL + " by cost desc";

   static String SQL_SELECT_ALL_SORTED_STATUS = SQL_SELECT_ALL + " by status";

   static String SQL_SELECT_ALL_SORTED_TIME = SQL_SELECT_ALL + " by time_of_completing";

   static String SQL_SELECT_BOOKS_BY_ID = "select books from orders where id = ?";

   static String SQL_SELECT_COST_OF_COMPLETED_WITHIN_PERIOD = "select cost from orders " +
           "where status = 'completed' " +
           "and (time_of_completing >= ? and time_of_completing <= ?)";

   static String SQL_UPDATE = "update orders set user = ?, cost = ?, status = ?, time-of-completing = ?, books = ?" +
           " where id = ?";

   static String SQL_INSERT = "insert into orders(user,cost,status,time_of_completing,books) " +
           "values (?, ?, ?, ?, ?))";



}
