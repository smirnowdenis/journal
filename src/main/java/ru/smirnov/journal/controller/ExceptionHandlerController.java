package ru.smirnov.journal.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;
import ru.smirnov.journal.exception.PerformanceNotFoundException;
import ru.smirnov.journal.exception.StudentNotFoundException;

import java.util.List;


@ControllerAdvice
public class ExceptionHandlerController {
    public static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerController.class);

    @ExceptionHandler({
            StudentNotFoundException.class,
            PerformanceNotFoundException.class
    })
    public Mono<Rendering> handleNotFoundException(RuntimeException e) {
        logger.debug("Handling exception: {0}", e);
        return Mono.just(Rendering.view("error")
                .modelAttribute("errors", e.getMessage())
                .status(HttpStatus.NOT_FOUND)
                .build());
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<List<String>> handleValidationException(WebExchangeBindException e) {
        logger.debug("Handling exception: {0}", e);
        List<String> errors = e.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
        return ResponseEntity.badRequest().body(errors);
    }
}
