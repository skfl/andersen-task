package com.andersentask.bookshop.order.service;

import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.book.services.BookService;
import com.andersentask.bookshop.order.entities.Order;
import com.andersentask.bookshop.order.enums.OrderSort;
import com.andersentask.bookshop.order.enums.OrderStatus;
import com.andersentask.bookshop.order.repositories.OrderRepository;
import com.andersentask.bookshop.request.entities.Request;
import com.andersentask.bookshop.request.services.RequestService;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;

    private final RequestService requestService;

    private final BookService bookService;

    public List<Order> getSortedOrders(OrderSort orderSort) {
        return orderRepository.findAll(Sort.by(Sort.Direction.ASC,
                orderSort.toString().toLowerCase(Locale.ROOT)));
    }

    public void saveOrder(Order order) {
        orderRepository.save(order);
        createRequestsFromOrder(order);
    }

    private void createRequestsFromOrder(Order order){
        List<Book> booksToRequest = bookService.getBooksOutOfStock(order.getBooks());
        for (Book book : booksToRequest) {
            Request request = Request.builder()
                    .book(book)
                    .build();
            requestService.saveRequest(request);
        }
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public Order changeStatusOfOrder(Long orderId, OrderStatus orderStatus) {
        Order orderToUpdate = orderRepository.findById(orderId).orElseThrow();
        if (fromInProcessToCompleted(orderToUpdate, orderStatus)) {
            orderToUpdate.setStatus(OrderStatus.COMPLETED);
            orderToUpdate.setTime(Timestamp.valueOf(LocalDateTime.now()));
            orderRepository.save(orderToUpdate);
        }
        if (fromInProcessToCanceled(orderToUpdate, orderStatus)) {
            orderToUpdate.setStatus(OrderStatus.CANCELED);
            orderRepository.save(orderToUpdate);
        }
        return orderToUpdate;
    }

    private boolean fromInProcessToCompleted(Order orderToUpdate, OrderStatus orderStatus) {
        return orderToUpdate.getStatus() == OrderStatus.IN_PROCESS
                && orderStatus == OrderStatus.COMPLETED;
    }

    private boolean fromInProcessToCanceled(Order orderToUpdate, OrderStatus orderStatus) {
        return orderToUpdate.getStatus() == OrderStatus.IN_PROCESS
                && orderStatus == OrderStatus.CANCELED;
    }

}
