package com.samsung.mes.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handle(Exception e, HttpServletRequest req) {
        e.printStackTrace(); // ✅ 무조건 콘솔에 원인 출력

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorBody(500, e.getClass().getName(), e.getMessage(), req.getRequestURI()));
    }

    record ErrorBody(int status, String error, String message, String path) {}
}
