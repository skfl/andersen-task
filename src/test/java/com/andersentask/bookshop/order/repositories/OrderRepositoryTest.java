package com.andersentask.bookshop.order.repositories;

import com.andersentask.bookshop.order.entities.Order;
import com.andersentask.bookshop.order.enums.OrderStatus;
import com.andersentask.bookshop.user.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class OrderRepositoryTest {

    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        orderRepository = new OrderRepository();
    }

    private final Order testOrder = Order.builder()
            .user(User.builder().firstName("qwerty").build())
            .orderCost(BigDecimal.valueOf(345.34))
            .orderStatus(OrderStatus.COMPLETED)
            .build();

    @Test
    void save() {
        orderRepository.save(testOrder);

        assertNotNull(testOrder.getOrderId());
        assertEquals(Optional.of(testOrder), orderRepository.findById(1L));
        assertEquals(1, orderRepository.findAll().size());
        assertTrue(orderRepository.findAll().contains(testOrder));
    }

    @Test
    void delete() {
        orderRepository.save(testOrder);
        orderRepository.delete(testOrder.getOrderId());

        assertEquals(OrderStatus.CANCELED, testOrder.getOrderStatus());
    }

    @Test
    void findById() {
        orderRepository.save(testOrder);
        Optional<Order> result = orderRepository.findById(testOrder.getOrderId());

        assertTrue(result.isPresent());
        assertEquals(testOrder, result.get());

        assertEquals(Optional.empty(), orderRepository.findById(2L));
    }

    @Test
    void findAll() {
        Order testOrder2 = Order.builder()
                .user(User.builder().firstName("qwerty2").build())
                .orderCost(BigDecimal.valueOf(3456.34))
                .orderStatus(OrderStatus.IN_PROCESS)
                .build();

        orderRepository.save(testOrder);
        orderRepository.save(testOrder2);

        List<Order> result = orderRepository.findAll();
        assertEquals(2, orderRepository.findAll().size());
        assertTrue(result.contains(testOrder));
        assertTrue(result.contains(testOrder2));
    }
}