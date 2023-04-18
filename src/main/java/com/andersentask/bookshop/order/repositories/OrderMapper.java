package com.andersentask.bookshop.order.repositories;

import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.order.entities.Order;
import com.andersentask.bookshop.order.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

@Slf4j
@AllArgsConstructor
@Getter
public class OrderMapper {

    static final Function<ResultSet, Order> orderMapper = resultSet -> {
        try {
            Long orderId = resultSet.getLong("id");
            BigDecimal orderCost = resultSet.getBigDecimal("cost");
            OrderStatus orderStatus = OrderStatus.valueOf(resultSet.getString("status"));
            LocalDateTime timeOfCompletingOrder = resultSet.getTimestamp("time_of_completing")
                    .toLocalDateTime();
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

}
