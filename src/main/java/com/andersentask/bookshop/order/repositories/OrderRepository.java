package com.andersentask.bookshop.order.repositories;

import com.andersentask.bookshop.order.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {

    void deleteById(Long id);

    boolean existsById(Long id);

}
