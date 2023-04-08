package com.andersentask.bookshop.order.repositories;

import com.andersentask.bookshop.common.AbstractCollectionRepository;
import com.andersentask.bookshop.order.entities.Order;
import com.andersentask.bookshop.order.enums.OrderStatus;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class OrderCollectionRepository implements AbstractCollectionRepository<Order,Long> {
    private final List<Order> orders;

    public OrderCollectionRepository() {
        this.orders = new ArrayList<>();
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
        orders.add(obj);
        return obj;
    }

    // The method just changes the status of order, but keeps object in the list
    @Override
    public void delete(Long id) {
        findById(id).ifPresentOrElse((x) -> x.setOrderStatus(OrderStatus.CANCELED), () -> {});
    }






}
