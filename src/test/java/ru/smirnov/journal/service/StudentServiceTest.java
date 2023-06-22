package ru.smirnov.journal.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.smirnov.journal.domain.Performance;
import ru.smirnov.journal.domain.Student;
import ru.smirnov.journal.repo.StudentRepo;

import java.sql.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {
    @Mock
    private StudentRepo studentRepo;

    @InjectMocks
    private StudentService studentService;

    @Test
    @DisplayName("Get student by Id")
    void getStudentById() {
        when(studentRepo.getById(any())).thenReturn(Mono.just(stubbedStudent()));

        studentService.getStudentById(1L)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    @DisplayName("Get all students")
    void getAllStudents() {
        when(studentRepo.getAllStudents()).thenReturn(Flux.just(stubbedStudent()));

        studentService.getAllStudent()
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    @DisplayName("Delete student by Id")
    void deleteStudentById() {
        when(studentRepo.getById(any())).thenReturn(Mono.just(stubbedStudent()));
        when(studentRepo.delete(any())).thenReturn(Mono.empty());

        studentService.delete(1L)
                .as(StepVerifier::create)
                .verifyComplete();
    }

    private Student stubbedStudent() {
        return Student.builder()
                .setId(1L)
                .setFullName("Иванов Иван")
                .setDateOfBirth(Date.valueOf("2010-10-15"))
                .setGrade(new Performance(5L, "отл"))
                .build();
    }
}
