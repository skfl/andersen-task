package com.andersentask.bookshop.order.service;


import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.book.services.BookService;
import com.andersentask.bookshop.order.entities.Order;
import com.andersentask.bookshop.order.enums.OrderStatus;
import com.andersentask.bookshop.order.repositories.OrderRepository;
import com.andersentask.bookshop.request.entities.Request;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    public void saveOrder(Order order) {
        orderRepository.save(order);
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public void completeOrder(Long id) {
        orderRepository.findById(id)
                .ifPresentOrElse(x -> {
                    x.setOrderStatus(OrderStatus.COMPLETED);
                    x.setTimeOfCompletingOrder(LocalDateTime.now());
                }, () -> {
                });
    }

    public void cancelOrder(Long id) {
        orderRepository.findById(id)
                .ifPresentOrElse(x -> x.setOrderStatus(OrderStatus.CANCELED)
                        , () -> {
                        });
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }


}
