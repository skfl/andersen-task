package com.andersentask.bookshop.order.service;

import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.order.entities.Order;
import com.andersentask.bookshop.order.enums.OrderSort;
import com.andersentask.bookshop.order.enums.OrderStatus;
import com.andersentask.bookshop.order.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public void changeStatusOfOrder(Long id, OrderStatus orderStatus) {
        Optional<Order> optionalOrder = getOrderById(id);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();

            switch (orderStatus) {
                case COMPLETED -> {
                    if (order.getOrderStatus() == OrderStatus.IN_PROCESS) {
                        order.setOrderStatus(OrderStatus.COMPLETED);
                        order.setTimeOfCompletingOrder(LocalDateTime.now());
                    }
                }
                case CANCELED -> {
                    if (order.getOrderStatus() == OrderStatus.IN_PROCESS) {
                        order.setOrderStatus(OrderStatus.CANCELED);
                    }
                }
            }
        }
    }

    public List<Order> getSortedOrders(OrderSort orderSort) {
        return orderRepository.getSortedOrders(orderSort);
    }

    public BigDecimal getIncomeForPeriod(LocalDateTime startOfPeriod, LocalDateTime endOfPeriod) {
        return getAllOrders().stream()
                .filter(order -> order.getOrderStatus() == OrderStatus.COMPLETED)
                .filter(order -> order.getTimeOfCompletingOrder().isAfter(startOfPeriod) &&
                        order.getTimeOfCompletingOrder().isBefore(endOfPeriod))
                .map(Order::getOrderCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<Book> getAllBooksFromOrder(Long id) {
        return orderRepository.findAllBooksFromOrder(id);
    }
}
