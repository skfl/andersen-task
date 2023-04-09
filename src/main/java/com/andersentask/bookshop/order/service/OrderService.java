package com.andersentask.bookshop.order.service;


import com.andersentask.bookshop.book.entities.Book;

import com.andersentask.bookshop.book.enums.BookStatus;
import com.andersentask.bookshop.order.entities.Order;
import com.andersentask.bookshop.order.enums.OrderStatus;
import com.andersentask.bookshop.order.repositories.OrderRepository;
import com.andersentask.bookshop.request.entities.Request;
import com.andersentask.bookshop.request.facade.RequestFacade;
import com.andersentask.bookshop.request.service.RequestService;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;


@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final RequestService requestService;
    private final RequestFacade requestFacade;


    /**
     * Initially order is created with List<Book>, that contains both available and out_of_stock books
     * The method divides list of books into 2 lists: 1L => available, 2L => out_of_stock
     * 1L is used to create order and save to DB
     * 2L is used to build request
     */
    public void createOrderAndRequest(Order order) {
        List<Book> books = order.getBooksInOrder();

        // build of request
        List<Book> booksToRequest = books.stream()
                .filter(x -> x.getStatus().equals(BookStatus.OUT_OF_STOCK))
                .toList();
        if (!booksToRequest.isEmpty()) {
            requestFacade.buildRequest(order.getUser(), booksToRequest);
        }

        // Creation of order
        List<Book> booksToOrder = books.stream()
                .filter(x -> x.getStatus().equals(BookStatus.AVAILABLE))
                .toList();
        Double orderCost = booksToOrder.stream()
                .map(Book::getPrice)
                .reduce(0D, Double::sum);
        if (!booksToOrder.isEmpty()) {
            orderRepository.save(Order.builder()
                    .user(order.getUser())
                    .orderCost(orderCost)
                    .orderStatus(OrderStatus.IN_PROCESS)
                    .timeOfCompletingOrder(LocalDateTime.now())
                    .booksInOrder(booksToOrder)
                    .build());
        }

    }

    /**
     * Method takes all requests and checks on books availability in each request
     * If request has all books available, the order is created
     */
    public void createOrderFromRequest() {
        List<Request> requestForOrder = requestService.checkRequestsToOrder();
        if (!requestForOrder.isEmpty()) {
            for (Request request : requestForOrder) {
                double requestCost = request.getRequestedBooks().stream()
                        .map(Book::getPrice)
                        .reduce(0D, Double::sum);

                orderRepository.save(Order.builder()
                        .user(request.getUser())
                        .orderCost(requestCost)
                        .orderStatus(OrderStatus.IN_PROCESS)
                        .timeOfCompletingOrder(LocalDateTime.now())
                        .booksInOrder(request.getRequestedBooks())
                        .build());
            }
        }
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

    public String getInfoAboutOrders() {
        return orderRepository.findAll().toString();
    }

    public double getIncomeForPeriod(LocalDateTime startOfPeriod, LocalDateTime endOfPeriod) {
        return getAllOrders().stream()
                .filter(x -> x.getOrderStatus().equals(OrderStatus.COMPLETED))
                .filter(x -> x.getTimeOfCompletingOrder().isAfter(startOfPeriod) &&
                        x.getTimeOfCompletingOrder().isBefore(endOfPeriod))
                .map(Order::getOrderCost)
                .reduce(0D, Double::sum);
    }


    public List<Order> getOrdersSortedByCost() {
        return getAllOrders().stream()
                .sorted(Comparator.comparing(Order::getOrderCost, Comparator.reverseOrder()))
                .toList();
    }


    public List<Order> getOrdersSortedByDate() {
        return getAllOrders().stream()
                .sorted(Comparator.comparing(Order::getTimeOfCompletingOrder, Comparator.reverseOrder()))
                .toList();
    }


    public List<Order> getOrdersSortedByStatus() {
        return getAllOrders().stream()
                .sorted(Comparator.comparingInt(x -> x.getOrderStatus().getOrdinalOfOrderEnum()))
                .toList();
    }


}
