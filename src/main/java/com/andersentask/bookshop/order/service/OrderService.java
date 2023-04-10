package com.andersentask.bookshop.order.service;


import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.order.entities.Order;
import com.andersentask.bookshop.order.enums.OrderStatus;
import com.andersentask.bookshop.order.repositories.OrderRepository;
import com.andersentask.bookshop.request.entities.Request;
import com.andersentask.bookshop.user.entities.User;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;


@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public void saveOrder(Order order) {
        if (!order.getBooksInOrder().isEmpty()) {
            orderRepository.save(order);
        }
    }

    //toDo: merge to one method
    private void createOrder(User user, List<Book> books) {

        //toDo: make method to calculate costOfBooksList to BookService
        //toDo: bookService to filter books on available/out_of_stock
            Double orderCost = books.stream()
                    .map(Book::getPrice)
                    .reduce(0D, Double::sum);

            orderRepository.save(Order.builder()
                    .user(user)
                    .orderCost(orderCost)
                    .orderStatus(OrderStatus.IN_PROCESS)
                    .timeOfCompletingOrder(null)
                    .booksInOrder(books)
                    .build());
    }

    //toDo: merge to one method
        public void createOrderFromRequest(List<Request> requestForOrder) {
            requestForOrder.forEach(x -> createOrder(x.getUser(),x.getRequestedBooks()));
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

    //toDo: to delete
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
