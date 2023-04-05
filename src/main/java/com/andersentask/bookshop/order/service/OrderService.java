package com.andersentask.bookshop.order.service;

import com.andersentask.bookshop.order.dtos.OrderDTO;
import com.andersentask.bookshop.order.exceptions.NoSuchOrderException;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {

    void createOrder(UserDTO userDTO, List<BookDTO> booksDTO);

    void createOrderFromRequest(RequestDTO requestDTO);

    void completeOrder(Long id);

    void cancelOrder(Long id) throws NoSuchOrderException;

    double getIncomeForPeriod(LocalDateTime startOfPeriod, LocalDateTime endOfPeriod);

    List<OrderDTO> getAllOrders();

    String getInfoAboutOrders();

    List<OrderDTO> getOrdersSorted();

    List<OrderDTO> getOrdersSortedByCost();

    List<OrderDTO> getOrdersSortedByDate();

    List<OrderDTO> getOrdersSortedByStatus();

}
