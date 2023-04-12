package com.andersentask.bookshop.order.service;


import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.book.enums.BookStatus;
import com.andersentask.bookshop.book.services.BookService;
import com.andersentask.bookshop.order.entities.Order;
import com.andersentask.bookshop.order.enums.OrderSort;
import com.andersentask.bookshop.order.enums.OrderStatus;
import com.andersentask.bookshop.order.repositories.OrderRepository;
import com.andersentask.bookshop.request.entities.Request;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
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


    public void changeStatusOfOrder(Long id, OrderStatus orderStatus) {
        Optional<Order> optionalOrder = getOrderById(id);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            switch (orderStatus) {
                case COMPLETED -> {
                    if (order.getOrderStatus().equals(OrderStatus.IN_PROCESS)) {
                        order.setOrderStatus(OrderStatus.COMPLETED);
                        order.setTimeOfCompletingOrder(LocalDateTime.now());
                    }
                }
                case CANCELED -> {
                    if (order.getOrderStatus().equals(OrderStatus.IN_PROCESS)) {
                        order.setOrderStatus(OrderStatus.CANCELED);
                    }
                }
                case IN_PROCESS -> {
                    if (order.getOrderStatus().equals(OrderStatus.CANCELED)) {
                        order.setOrderStatus(OrderStatus.IN_PROCESS);
                    }
                }
            }
        }
    }

    public List<Order> getSortedOrders(OrderSort orderSort) {
        List<Order> orders = getAllOrders();
        List<Order> ordersToReturn = new ArrayList<>();
        switch (orderSort) {
            case COST -> ordersToReturn = orders.stream()
                    .sorted(Comparator.comparing(Order::getOrderCost))
                    .toList();
            case COMPLETION_DATE -> ordersToReturn = orders.stream()
                    .sorted(Comparator.comparing(Order::getTimeOfCompletingOrder).reversed())
                    .toList();
            case STATUS -> ordersToReturn = orders.stream()
                    .sorted(Comparator.comparing(x -> x.getOrderStatus().ordinal()))
                    .toList();
        }
        return ordersToReturn;
    }

    public double getIncomeForPeriod(LocalDateTime startOfPeriod, LocalDateTime endOfPeriod) {
        return getAllOrders().stream()
                .filter(x -> x.getOrderStatus().equals(OrderStatus.COMPLETED))
                .filter(x -> x.getTimeOfCompletingOrder().isAfter(startOfPeriod) &&
                        x.getTimeOfCompletingOrder().isBefore(endOfPeriod))
                .map(Order::getOrderCost)
                .reduce(0D, Double::sum);
    }

    public List<Book> getAllBooksFromOrder(Long id) {
        List<Book> books = new ArrayList<>();
        Optional<Order> optionalOrder = getOrderById(id);
        if (optionalOrder.isPresent()) {
        books = optionalOrder.get().getBooksInOrder();
        }
        return books;
    }

}
