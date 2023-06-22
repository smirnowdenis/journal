package ru.smirnov.journal.exception;

public class PerformanceNotFoundException extends RuntimeException {
    public PerformanceNotFoundException(Long id) {
        super(String.format("Performance with ID: %d not found", id));
    }
}
