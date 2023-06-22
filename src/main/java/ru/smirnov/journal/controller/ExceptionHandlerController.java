package ru.smirnov.journal.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import ru.smirnov.journal.exception.PerformanceNotFoundException;
import ru.smirnov.journal.exception.StudentNotFoundException;

import java.util.List;

@RestControllerAdvice
public class ExceptionHandlerController {
    public static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerController.class);

    @ExceptionHandler({
            StudentNotFoundException.class,
            PerformanceNotFoundException.class
    })
    ResponseEntity<String> handleNotFoundException(RuntimeException e) {
        logger.debug("Handling exception: {}", e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<List<String>> handleValidationException(WebExchangeBindException e) {
        logger.debug("Handling exception: {}", e);
        List<String> errors = e.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
        return ResponseEntity.badRequest().body(errors);
    }
}
