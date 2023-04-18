package com.andersentask.bookshop.order.repositories;

import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.book.repositories.BookRepository;
import com.andersentask.bookshop.book.services.BookService;
import com.andersentask.bookshop.common.CollectionRepository;
import com.andersentask.bookshop.order.entities.Order;
import com.andersentask.bookshop.order.enums.OrderSort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
public class OrderRepository implements CollectionRepository<Order, Long> {

    private final DataSource dataSource;

    private final BookRepository bookRepository;

    private final OrderMapper orderMapper;

    //done
    @Override
    public Optional<Order> findById(Long orderId) {
        List<Book> booksInOrder = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(OrderSQLCommands.SQL_SELECT_BY_ID)) {
            statement.setLong(1, orderId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ?
                        Optional.of(OrderMapper.mapper.apply(resultSet)) : Optional.empty();
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException(e);
        }
    }


    //done
    @Override
    public List<Order> findAll() {
        List<Order> orders = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(OrderSQLCommands.SQL_SELECT_ALL);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Order order = orderMapper.toMap(resultSet);

            }
        return orders;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException(e);
        }
    }



    @Override
    public Order save(Order obj) {
        try (Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(OrderSQLCommands.SQL_INSERT
                    , Statement.RETURN_GENERATED_KEYS)){

            statement.setObject(1,obj.getUser());
            statement.setBigDecimal(2,obj.getOrderCost());
            statement.setString(3,obj.getOrderStatus().toString());
            statement.setTimestamp(4, Timestamp.valueOf(obj.getTimeOfCompletingOrder()));


        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException(e);
        }
    }

    public List<Book> findAllBooksFromOrder(Long id) {
        return findById(id).map(Order::getBooksInOrder)
                .orElse(new ArrayList<>());
    }

    public List<Order> getSortedOrders(OrderSort orderSort) {
        switch (orderSort) {
            case COST -> {
                return orders.stream()
                        .sorted(Comparator.comparing(Order::getOrderCost))
                        .toList();
            }
            case COMPLETION_DATE -> {
                return orders.stream()
                        .sorted(Comparator.comparing(Order::getTimeOfCompletingOrder).reversed())
                        .toList();
            }
            case STATUS -> {
                return orders.stream()
                        .sorted(Comparator.comparing(order -> order.getOrderStatus().ordinal()))
                        .toList();
            }
            case ID -> {
                return orders.stream()
                        .sorted(Comparator.comparing(Order::getOrderId))
                        .toList();
            }
        }
        return orders;
    }

}
