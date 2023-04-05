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
    private String id;

    private User user;

    private Book requestedBook;

    private RequestStatus requestStatus;

    private LocalDateTime createdAt;
}
