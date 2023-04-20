package com.andersentask.bookshop.order.repositories;

import com.andersentask.bookshop.order.entities.Order;
import com.andersentask.bookshop.order.enums.OrderStatus;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.function.Function;

@Slf4j
class OrderMapper {

    static final Function<ResultSet, Order> mapper = resultSet -> {
        try {
            Long orderId = resultSet.getLong("id");
            BigDecimal orderCost = resultSet.getBigDecimal("cost");
            OrderStatus orderStatus = OrderStatus.valueOf(resultSet.getString("status"));
            Timestamp timeOfCompletingOrder = null;
            if (resultSet.getTimestamp("time_of_completing") != null) {
                timeOfCompletingOrder = resultSet.getTimestamp("time_of_completing");
            }
            return Order.builder()
                    .orderId(orderId)
                    .orderCost(orderCost)
                    .orderStatus(orderStatus)
                    .timeOfCompletingOrder(timeOfCompletingOrder)
                    .booksInOrder(new ArrayList<>())
                    .build();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException(e);
        }
    };

    private OrderMapper() {
    }
}
