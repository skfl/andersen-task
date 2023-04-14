package com.andersentask.bookshop.order.repositories;

import com.andersentask.bookshop.common.CollectionRepository;
import com.andersentask.bookshop.order.entities.Order;
import com.andersentask.bookshop.order.enums.OrderStatus;

import java.util.ArrayList;
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
        for (Order order : orders) {
            if (order.getOrderId().equals(id)) {
                return Optional.of(order);
            }
        }
        return Optional.empty();
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

    @Override
    public void delete(Long id) {
        findById(id).ifPresent(x -> x.setOrderStatus(OrderStatus.CANCELED));
    }
}
