package com.andersentask.bookshop.request.dtos;

import com.andersentask.bookshop.request.entities.enums.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class RequestDTO {
    private Long id;

    private User user;

    private List<Book> requestedBooks;

    private RequestStatus requestStatus;

    private LocalDateTime createdAt;
}
