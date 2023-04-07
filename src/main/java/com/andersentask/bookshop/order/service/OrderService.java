package com.andersentask.bookshop.order.service;


import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.book.enums.BookStatus;
import com.andersentask.bookshop.order.entities.Order;
import com.andersentask.bookshop.order.enums.OrderStatus;
import com.andersentask.bookshop.order.repositories.OrderCollectionRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;


@RequiredArgsConstructor
@Data
public class OrderService {

    private final OrderCollectionRepository orderRepository;

    public void createOrder(Order order) {
        List<Book> books = order.getBooksInOrder();

        List<Book> booksToRequest = books.stream()
                .filter((x) -> x.getStatus().equals(BookStatus.OUT_OF_STOCK))
                .toList();
        // Method, that creates request
        if (booksToRequest.size() > 0) {
            //makeRequest(orderDTO.getUser(), booksToRequest);
        }

        List<Book> booksToOrder = books.stream()
                .filter((x) -> x.getStatus().equals(BookStatus.AVAILABLE))
                .toList();
        Double orderCost = booksToOrder.stream()
                .map(Book::getPrice)
                .reduce(0D, Double::sum);
        orderRepository.save(Order.builder()
                .user(order.getUser())
                .orderCost(orderCost)
                .orderStatus(OrderStatus.IN_PROCESS)
                .timeOfCompletingOrder(LocalDateTime.now())
                .booksInOrder(booksToOrder)
                .build());
    }


    // The method takes request and adds it to the order with status "in process"
    // The method does not check, if books are available or not
//    public void createOrderFromRequest(Request request) {
//
//        double requestCost = request.getBooksInRequest().stream()
//                .map(Book::getPrice)
//                .reduce(0D, Double::sum);
//
//        orderRepository.save(Order.builder()
//                .user(request.getUser())
//                .orderCost(requestCost)
//                .orderStatus(OrderStatus.IN_PROCESS)
//                .timeOfCompletingOrder(LocalDateTime.now())
//                .booksInOrder(request.getBooksInRequest())
//                .build());
//    }

    public void completeOrder(Long id) {
        orderRepository.findById(id)
                .ifPresentOrElse((x) -> {
                            x.setOrderStatus(OrderStatus.COMPLETED);
                            x.setTimeOfCompletingOrder(LocalDateTime.now());
                        },() -> {});
    }


    public void cancelOrder(Long id) {
        orderRepository.findById(id)
                        .ifPresentOrElse((x) -> x.setOrderStatus(OrderStatus.CANCELED)
                                ,() -> {});
    }


    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public String getInfoAboutOrders() {
        return orderRepository.findAll().toString();
    }

    public double getIncomeForPeriod(LocalDateTime startOfPeriod, LocalDateTime endOfPeriod) {
        return getAllOrders().stream()
                .filter((x) -> x.getOrderStatus().equals(OrderStatus.COMPLETED))
                .filter((x) -> x.getTimeOfCompletingOrder().isAfter(startOfPeriod) &&
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
