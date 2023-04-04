package com.andersentask.bookshop.mappers;

import com.andersentask.bookshop.dtos.BookDTO;
import com.andersentask.bookshop.entities.Book;

import java.util.List;

public class BookMapper {

    public static BookDTO entityToDto(Book book) {
        return BookDTO.builder()
                .id(book.getId())
                .name(book.getName())
                .price(book.getPrice())
                .status(book.getStatus())
                .build();
    }

    public static Book dtoToEntity(BookDTO dto) {
        return Book.builder()
                .id(dto.getId())
                .name(dto.getName())
                .price(dto.getPrice())
                .status(dto.getStatus())
                .build();
    }

    public static List<BookDTO> entityListToDtoList(List<Book> entities) {
        return entities.stream().map(BookMapper::entityToDto).toList();
    }

    public static List<Book> dtoListToEntityList(List<BookDTO> dtos) {
        return dtos.stream().map(BookMapper::dtoToEntity).toList();
    }
}
