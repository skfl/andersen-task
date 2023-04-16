package com.andersentask.bookshop.order.repositories;

import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.common.CollectionRepository;
import com.andersentask.bookshop.order.entities.Order;
import com.andersentask.bookshop.order.enums.OrderSort;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class OrderRepository implements CollectionRepository<Order, Long> {
    private final List<Order> orders;
    private Long id;

    public OrderRepository() {
        this.orders = new ArrayList<>();
        this.id = 1L;
    }

    @Override
    public Optional<Order> findById(Long id) {
        return orders.stream()
                .filter(order -> order.getOrderId().equals(id))
                .findAny();
    }

    @Override
    public List<Order> findAll() {
        return this.orders;
    }

    @Override
    public Order save(Order obj) {
        obj.setOrderId(id++);
        orders.add(obj);
        return obj;
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
                        .sorted(Comparator.comparing(x -> x.getOrderStatus().ordinal()))
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
