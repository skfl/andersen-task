package com.andersentask.bookshop.order.repositories;

public class OrderJPQLQueries {

    static final String SQL_SELECT_ALL = "select o from Order o";

    static final String SQL_SELECT_BY_ID = SQL_SELECT_ALL +
            " where id = :id";

    static final String SQL_SELECT_ALL_SORTED_ID = SQL_SELECT_ALL +
            " order by o.id";

    static final String SQL_SELECT_ALL_SORTED_COST = SQL_SELECT_ALL +
            " order by o.orderCost";

    static final String SQL_SELECT_ALL_SORTED_STATUS = SQL_SELECT_ALL
            + " order by o.orderStatus";

    static final String SQL_SELECT_ALL_SORTED_TIME = SQL_SELECT_ALL +
            " order by o.timeOfCompletingOrder desc";

    static final String SQL_SELECT_COST_OF_COMPLETED_WITHIN_PERIOD = "select sum(cost) as total_cost from Order o " +
            "where o.orderStatus = 'completed' " +
            "and (o.timeOfCompletingOrder >= :time_of_completing" +
            " and o.timeOfCompletingOrderg <= :time_of_completing)";

    private OrderJPQLQueries() {
    }
}
