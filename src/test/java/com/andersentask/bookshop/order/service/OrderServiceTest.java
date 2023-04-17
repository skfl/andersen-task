package com.andersentask.bookshop.order.service;

import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.book.enums.BookStatus;
import com.andersentask.bookshop.order.entities.Order;
import com.andersentask.bookshop.order.enums.OrderSort;
import com.andersentask.bookshop.order.enums.OrderStatus;
import com.andersentask.bookshop.order.repositories.OrderRepository;
import com.andersentask.bookshop.user.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderRepository = new OrderRepository();
        orderService = new OrderService(orderRepository);
    }

    private final Order testOrder = Order.builder()
            .user(User.builder().firstName("qwerty").build())
            .orderCost(BigDecimal.valueOf(345.34))
            .orderStatus(OrderStatus.COMPLETED)
            .timeOfCompletingOrder(LocalDateTime.now())
            .booksInOrder(List.of(Book.builder()
                    .name("qwerty")
                    .status(BookStatus.AVAILABLE)
                    .price(BigDecimal.valueOf(12.12))
                    .build()))
            .build();

    private final Order testOrder2 = Order.builder()
            .user(User.builder().firstName("qwerty2").build())
            .orderCost(BigDecimal.valueOf(3456.34))
            .orderStatus(OrderStatus.IN_PROCESS)
            .timeOfCompletingOrder(LocalDateTime.now())
            .build();

    private final Order testOrder3 = Order.builder()
            .user(User.builder().firstName("qwerty3").build())
            .orderCost(BigDecimal.valueOf(3106.34))
            .orderStatus(OrderStatus.CANCELED)
            .timeOfCompletingOrder(LocalDateTime.now())
            .build();

    @Test
    void saveOrder() {
        orderService.saveOrder(testOrder);

        assertNotNull(testOrder.getOrderId());
        assertEquals(Optional.of(testOrder), orderService.getOrderById(testOrder.getOrderId()));
        assertEquals(1, orderService.getAllOrders().size());
        assertTrue(orderService.getAllOrders().contains(testOrder));
    }

    @Test
    void getOrderById() {
        orderService.saveOrder(testOrder);
        Optional<Order> result = orderService.getOrderById(testOrder.getOrderId());

        assertTrue(result.isPresent());
        assertEquals(testOrder, result.get());
        assertEquals(Optional.empty(), orderService.getOrderById(2L));
    }

    @Test
    void getAllOrders() {
        orderService.saveOrder(testOrder);
        orderService.saveOrder(testOrder2);
        List<Order> bookList = orderService.getAllOrders();

        assertEquals(2, orderService.getAllOrders().size());
        assertTrue(bookList.contains(testOrder));
        assertTrue(bookList.contains(testOrder2));
    }

    @Test
    void changeStatusOfOrder() {
        orderService.saveOrder(testOrder);
        orderService.saveOrder(testOrder2);
        orderService.saveOrder(testOrder3);

        orderService.changeStatusOfOrder(testOrder.getOrderId(), OrderStatus.IN_PROCESS);
        assertNotEquals(OrderStatus.IN_PROCESS, testOrder.getOrderStatus());

        orderService.changeStatusOfOrder(testOrder2.getOrderId(), OrderStatus.COMPLETED);
        assertEquals(OrderStatus.COMPLETED, testOrder2.getOrderStatus());

        orderService.changeStatusOfOrder(testOrder3.getOrderId(), OrderStatus.IN_PROCESS);
        assertEquals(OrderStatus.CANCELED, testOrder3.getOrderStatus());
    }

    @Test
    void getSortedOrders() {
        orderService.saveOrder(testOrder);
        orderService.saveOrder(testOrder2);

        orderService.getSortedOrders(OrderSort.ID);
        assertEquals(1L, orderService.getAllOrders().get(0).getOrderId());
        assertEquals(2L, orderService.getAllOrders().get(1).getOrderId());

        orderService.getSortedOrders(OrderSort.COMPLETION_DATE);
        assertNotEquals(LocalDateTime.now(), orderService.getAllOrders().get(0).getTimeOfCompletingOrder());
        assertNotEquals(LocalDateTime.now(), orderService.getAllOrders().get(1).getTimeOfCompletingOrder());

        orderService.getSortedOrders(OrderSort.COST);
        assertEquals(BigDecimal.valueOf(345.34), orderService.getAllOrders().get(0).getOrderCost());
        assertEquals(BigDecimal.valueOf(3456.34), orderService.getAllOrders().get(1).getOrderCost());

        orderService.getSortedOrders(OrderSort.STATUS);
        assertEquals(OrderStatus.COMPLETED, orderService.getAllOrders().get(0).getOrderStatus());
        assertEquals(OrderStatus.IN_PROCESS, orderService.getAllOrders().get(1).getOrderStatus());
    }

    @Test
    void getIncomeForPeriod() {
        Order completedOrder1 = Order.builder()
                .user(User.builder().firstName("qwerty").build())
                .orderCost(BigDecimal.valueOf(345.34))
                .orderStatus(OrderStatus.COMPLETED)
                .timeOfCompletingOrder(LocalDateTime.of(2023, 1, 1, 2, 2))
                .booksInOrder(List.of(Book.builder()
                        .name("qwerty")
                        .status(BookStatus.AVAILABLE)
                        .price(BigDecimal.valueOf(12.12))
                        .build()))
                .build();
        Order completedOrder2 = Order.builder()
                .user(User.builder().firstName("qwerty").build())
                .orderCost(BigDecimal.valueOf(345.34))
                .orderStatus(OrderStatus.COMPLETED)
                .timeOfCompletingOrder(LocalDateTime.of(2023, 2, 2, 2, 2))
                .booksInOrder(List.of(Book.builder()
                        .name("qwerty")
                        .status(BookStatus.AVAILABLE)
                        .price(BigDecimal.valueOf(12.12))
                        .build()))
                .build();

        orderService.saveOrder(completedOrder1);
        orderService.saveOrder(completedOrder2);


        assertEquals(BigDecimal.valueOf(345.34), orderService.getIncomeForPeriod(LocalDateTime.of(2023,1,1,1,1), LocalDateTime.of(2023, 2, 1, 1, 1)));
        assertEquals(BigDecimal.valueOf(690.68), orderService.getIncomeForPeriod(LocalDateTime.of(2023,1,1,1,1), LocalDateTime.of(2023, 3, 1, 1, 1)));
        assertEquals(BigDecimal.ZERO, orderService.getIncomeForPeriod(LocalDateTime.of(2024,1,1,1,1), LocalDateTime.of(2023, 3, 1, 1, 1)));

    }

    @Test
    void getAllBooksFromOrder() {
        orderService.saveOrder(testOrder);

        assertEquals(testOrder.getBooksInOrder(), orderService.getAllBooksFromOrder(testOrder.getOrderId()));
    }
}