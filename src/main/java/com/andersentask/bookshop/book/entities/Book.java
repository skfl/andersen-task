package com.andersentask.bookshop.book.entities;

import com.andersentask.bookshop.book.enums.BookStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String name;

    @Enumerated(value = EnumType.STRING)
    @Column
    private BookStatus status;

    @Column
    private Double price;
}
