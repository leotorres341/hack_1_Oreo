package com.example.hackaton2;


import com.example.hackaton2.exception.ForbiddenException;
import com.example.hackaton2.exception.NotFoundException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.naming.ServiceUnavailableException;
import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private Map<String, Object> body(String code, String message, String path) {
        return Map.of(
                "error", code,
                "message", message,
                "timestamp", Instant.now().toString(),
                "path", path == null ? "" : path
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> badRequest(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(body("BAD_REQUEST", ex.getMessage(), ""));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> validation(MethodArgumentNotValidException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(body("BAD_REQUEST", ex.getBindingResult().getAllErrors().get(0).getDefaultMessage(), ""));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<?> forbidden(ForbiddenException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(body("FORBIDDEN", ex.getMessage(), ""));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> notFound(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(body("NOT_FOUND", ex.getMessage(), ""));
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<?> svcUnavailable(ServiceUnavailableException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(body("SERVICE_UNAVAILABLE", ex.getMessage(), ""));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> generic(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(body("SERVER_ERROR", ex.getMessage(), ""));
    }
}