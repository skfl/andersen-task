package com.andersentask.bookshop.book.entities;

import com.andersentask.bookshop.book.enums.BookStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Book {
    private Long id;

    private String name;

    private BookStatus status;

    private Double price;
}
