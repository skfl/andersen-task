package com.andersentask.bookshop.response;

import lombok.*;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationError implements Serializable {

    private int statusCode;

    private String errorMessage;
}
