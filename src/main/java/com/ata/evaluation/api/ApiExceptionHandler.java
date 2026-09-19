package com.ata.evaluation.api;

import java.util.Map;
import java.util.NoSuchElementException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
class ApiExceptionHandler {
    @ExceptionHandler({IllegalArgumentException.class, NoSuchElementException.class})
    ResponseEntity<Map<String, String>> invalid(RuntimeException exception) { return ResponseEntity.status(exception instanceof NoSuchElementException ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST).body(Map.of("error", exception.getMessage())); }
}
