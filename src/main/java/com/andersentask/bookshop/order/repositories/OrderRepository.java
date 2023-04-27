package com.andersentask.bookshop.order.repositories;

import com.andersentask.bookshop.order.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
}
