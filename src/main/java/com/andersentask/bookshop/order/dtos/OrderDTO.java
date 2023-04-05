package com.andersentask.bookshop.order.dtos;

import com.andersentask.bookshop.order.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class OrderDTO {

    private Long id;

    // to update
    private UserDTO user;

    private double orderCost;

    private OrderStatus orderStatus;

    private LocalDateTime timeOfCompletingOrder;

    private List<BookDTO> booksInOrder;

}
