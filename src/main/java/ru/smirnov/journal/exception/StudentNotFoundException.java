package ru.smirnov.journal.exception;

public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(Long id) {
        super(String.format("Student with ID: %d not found", id));
    }
}
