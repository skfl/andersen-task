package com.andersentask.bookshop.order.entities;


import com.andersentask.bookshop.order.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.print.Book;  // to delete
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    @ManyToOne(cascade = CascadeType.ALL) // to reconcile
    @JoinColumn(name = "user_id") // to change
    private User user;

    @Column
    private double orderCost;

    @Column
    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime timeOfCompletingOrder;

    @ManyToMany(cascade = CascadeType.ALL) // to agree
    @JoinTable(name = "name of table", // separate table for order id and book id
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id"))
    private List<Book> booksInOrder;

}
