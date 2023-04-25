package com.andersentask.bookshop.exception;


import com.andersentask.bookshop.response.ApplicationError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ApplicationError> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT.value()).body(ApplicationError.builder()
                .statusCode(HttpStatus.I_AM_A_TEAPOT.value())
                .errorMessage(e.getMessage())
                .build());
    }
}
