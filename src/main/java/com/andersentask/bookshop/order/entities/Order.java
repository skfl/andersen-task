package com.andersentask.bookshop.order.entities;


import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.order.enums.OrderStatus;
import com.andersentask.bookshop.user.entities.User;
import lombok.*;


import java.time.LocalDateTime;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    private Long orderId;
    private User user;
    private double orderCost;
    private OrderStatus orderStatus;
    private LocalDateTime timeOfCompletingOrder;
    private List<Book> booksInOrder;

}
