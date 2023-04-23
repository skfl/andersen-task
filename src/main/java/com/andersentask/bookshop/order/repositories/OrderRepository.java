package com.andersentask.bookshop.order.repositories;

import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.order.entities.Order;
import com.andersentask.bookshop.order.enums.OrderSort;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
public class OrderRepository {

    private final EntityManager entityManager;

    public Optional<Order> findById(Long orderId) {
        TypedQuery<Order> query = entityManager.createQuery(OrderJPQLQueries.SQL_SELECT_BY_ID,Order.class);
        query.setParameter("id", orderId);
        return query.getResultStream().findFirst();
    }

    public List<Order> findAll() {
        TypedQuery<Order> query = entityManager.createQuery(OrderJPQLQueries.SQL_SELECT_ALL, Order.class);
        return query.getResultList();
    }

    public List<Book> findAllBooksFromOrder(Long id) {
        return findById(id).map(Order::getBooks)
                .orElse(new ArrayList<>());
    }

    public List<Order> findSortedOrders(OrderSort orderSort) {
        String queryString = "";
        switch (orderSort) {
            case COST -> queryString = OrderJPQLQueries.SQL_SELECT_ALL_SORTED_COST;
            case STATUS -> queryString = OrderJPQLQueries.SQL_SELECT_ALL_SORTED_STATUS;
            case COMPLETION_DATE -> queryString = OrderJPQLQueries.SQL_SELECT_ALL_SORTED_TIME;
            case ID -> queryString = OrderJPQLQueries.SQL_SELECT_ALL_SORTED_ID;
        }
        TypedQuery<Order> query = entityManager.createQuery(queryString, Order.class);
        return query.getResultList();
    }

    public Order save(Order order) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(order);
        transaction.commit();
        return order;
    }

    public void update(Order order) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Order orderToUpdate = entityManager.getReference(Order.class, order.getOrderId());
        orderToUpdate.setOrderCost(order.getOrderCost());
        orderToUpdate.setTimeOfCompletingOrder(order.getTimeOfCompletingOrder());
        orderToUpdate.setOrderStatus(order.getOrderStatus());
        orderToUpdate.setUserId(order.getUserId());
        orderToUpdate.setBooks(order.getBooks());
        entityManager.merge(orderToUpdate);
        transaction.commit();
    }

    public BigDecimal findIncomeForPeriod(LocalDateTime startOfPeriod, LocalDateTime endOfPeriod) {
        TypedQuery<BigDecimal> query = entityManager.createQuery(OrderJPQLQueries.SQL_SELECT_COST_OF_COMPLETED_WITHIN_PERIOD
                ,BigDecimal.class);
        query.setParameter("time_of_completing", startOfPeriod);
        query.setParameter("time_of_completing",endOfPeriod);
        return query.getResultList().get(0);
    }
}
