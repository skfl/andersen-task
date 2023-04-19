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

    public void changeStatusOfOrder(Long orderId, OrderStatus orderStatus) {
        Order orderToUpdate = orderRepository.findById(orderId).orElseThrow();
        if (fromInProcessToCompleted(orderToUpdate, orderStatus)) {
            orderToUpdate.setOrderStatus(OrderStatus.COMPLETED);
            orderToUpdate.setTimeOfCompletingOrder(LocalDateTime.now());
            orderRepository.update(orderToUpdate);
        }
        if (fromInProcessToCanceled(orderToUpdate, orderStatus)) {
            orderToUpdate.setOrderStatus(OrderStatus.CANCELED);
            orderRepository.update(orderToUpdate);
        }
    }

    private boolean fromInProcessToCompleted(Order orderToUpdate, OrderStatus orderStatus) {
        return orderToUpdate.getOrderStatus() == OrderStatus.IN_PROCESS
                && orderStatus == OrderStatus.COMPLETED;
    }

    private boolean fromInProcessToCanceled(Order orderToUpdate, OrderStatus orderStatus) {
        return orderToUpdate.getOrderStatus() == OrderStatus.IN_PROCESS
                && orderStatus == OrderStatus.CANCELED;
    }


    public List<Order> getSortedOrders(OrderSort orderSort) {
        return orderRepository.findSortedOrders(orderSort);
    }

    public BigDecimal getIncomeForPeriod(LocalDateTime startOfPeriod, LocalDateTime endOfPeriod) {
        return orderRepository.findIncomeForPeriod(startOfPeriod,endOfPeriod);
    }

    public List<Book> getAllBooksFromOrder(Long id) {
        return orderRepository.findAllBooksFromOrder(id);
    }
}
