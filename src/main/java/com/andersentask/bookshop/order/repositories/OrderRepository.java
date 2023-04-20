package com.andersentask.bookshop.order.repositories;

import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.book.services.BookService;
import com.andersentask.bookshop.order.entities.Order;
import com.andersentask.bookshop.order.enums.OrderSort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
public class OrderRepository {

    private final DataSource dataSource;

    private final BookService bookService;

    public Optional<Order> findById(Long orderId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(OrderSQLCommands.SQL_SELECT_BY_ID)) {
            statement.setLong(1, orderId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Order order = findOrder(resultSet, orderId, connection);
                    return Optional.of(order);
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException(e);
        }
    }

    public List<Order> findAll() {
        List<Order> orders = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(OrderSQLCommands.SQL_SELECT_ALL);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Long orderId = resultSet.getLong("id");
                Order order = findOrder(resultSet, orderId, connection);
                orders.add(order);
            }
            return orders;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException(e);
        }
    }

    public List<Book> findAllBooksFromOrder(Long id) {
        return findById(id).map(Order::getBooksInOrder)
                .orElse(new ArrayList<>());
    }

    public List<Order> findSortedOrders(OrderSort orderSort) {
        ArrayList<Order> sortedOrders = new ArrayList<>();
        String sqlSort = OrderSQLCommands.SQL_SELECT_ALL;
        switch (orderSort) {
            case COST -> sqlSort = OrderSQLCommands.SQL_SELECT_ALL_SORTED_COST;
            case COMPLETION_DATE -> sqlSort = OrderSQLCommands.SQL_SELECT_ALL_SORTED_TIME;
            case STATUS -> sqlSort = OrderSQLCommands.SQL_SELECT_ALL_SORTED_STATUS;
            case ID -> sqlSort = OrderSQLCommands.SQL_SELECT_ALL;
        }
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlSort)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long orderId = resultSet.getLong("id");
                Order order = findOrder(resultSet, orderId, connection);
                sortedOrders.add(order);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException(e);
        }
        return sortedOrders;
    }

    private Order findOrder(ResultSet resultSet, Long orderId, Connection connection) {
        Order order = OrderMapper.mapper.apply(resultSet);
        Long numberOfBooksInOrder = countBooksInOneOrder(orderId, connection);
        List<Book> booksInOrder = findBooks(resultSet, numberOfBooksInOrder);
        order.setBooksInOrder(booksInOrder);
        return order;
    }

    private Long countBooksInOneOrder(Long orderId, Connection connection) {
        try (PreparedStatement statement = connection.prepareStatement(OrderBooksSQLCommands.SQL_COUNT_BY_ID)) {
            statement.setLong(1, orderId);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong("count");
            }
            return 0L;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException(e);
        }
    }

    private List<Book> findBooks(ResultSet resultSet, Long numberOfBooksInOrder) {
        List<Book> books = new ArrayList<>();
        try {
            books.add(findBook(resultSet));
            for (int i = 0; i < numberOfBooksInOrder - 1; i++) {
                resultSet.next();
                books.add(findBook(resultSet));
            }
            return books;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException(e);
        }
    }

    private Book findBook(ResultSet resultSet) {
        try {
            Long bookId = resultSet.getLong("book_id");
            return bookService.getBookById(bookId).orElseThrow();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException(e);
        }
    }

    public Order save(Order obj) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(OrderSQLCommands.SQL_INSERT
                     , Statement.RETURN_GENERATED_KEYS)) {

            List<Book> booksInOrder = obj.getBooksInOrder();
            int indexNumberForStatement = 4;
            setFieldsOfStatement(obj, statement, indexNumberForStatement);

            int affectedRows = statement.executeUpdate();
            if (affectedRows != 1) {
                throw new SQLException();
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    Long orderId = generatedKeys.getLong("id");
                    saveBookIds(orderId, booksInOrder);
                    return findById(orderId).orElseThrow();
                } else {
                    throw new SQLException();
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException(e);
        }
    }

    private void setFieldsOfStatement(Order obj, PreparedStatement statement, int indexNumberOfStatement) throws SQLException {
        statement.setLong(1, 0L);
        if (obj.getUser() != null) {
            statement.setLong(1, obj.getUser().getId());
        }
        statement.setBigDecimal(2, obj.getOrderCost());
        statement.setString(3, obj.getOrderStatus().toString());
        statement.setTimestamp(4, null);
        if (obj.getTimeOfCompletingOrder() != null) {
            statement.setTimestamp(4, obj.getTimeOfCompletingOrder());
        }
        if (indexNumberOfStatement == 5) {
            statement.setLong(5, obj.getOrderId());
        }
    }

    private void saveBookIds(Long orderId, List<Book> booksInOrder) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(OrderBooksSQLCommands.SQL_INSERT)) {
            for (Book book : booksInOrder) {
                statement.setLong(1, orderId);
                statement.setLong(2, book.getId());

                int affectedRows = statement.executeUpdate();
                if (affectedRows != 1) {
                    throw new SQLException();
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException(e);
        }
    }

    public void update(Order order) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(OrderSQLCommands.SQL_UPDATE)) {

            int indexNumberForStatement = 5;
            setFieldsOfStatement(order, statement, indexNumberForStatement);

            int affectedRows = statement.executeUpdate();
            if (affectedRows != 1) {
                throw new SQLException();
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException(e);
        }
    }

    public BigDecimal findIncomeForPeriod(LocalDateTime startOfPeriod, LocalDateTime endOfPeriod) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement
                     (OrderSQLCommands.SQL_SELECT_COST_OF_COMPLETED_WITHIN_PERIOD)) {
            statement.setTimestamp(1, Timestamp.valueOf(startOfPeriod));
            statement.setTimestamp(2, Timestamp.valueOf(endOfPeriod));
            var resultSet = statement.executeQuery();
            return resultSet.getBigDecimal("total_cost");
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException(e);
        }
    }

}
