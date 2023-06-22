package ru.smirnov.journal.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import reactor.test.StepVerifier;
import ru.smirnov.journal.domain.Performance;
import ru.smirnov.journal.domain.Student;
import ru.smirnov.journal.exception.StudentNotFoundException;
import ru.smirnov.journal.repo.StudentRepo;

import java.sql.Date;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class StudentRepoTest {
    @Autowired
    private StudentRepo studentRepo;

    @Test
    @DisplayName("Add student")
    void addStudent() {
        Student student = Student.builder()
                .setId(1L)
                .setFullName("Nick Nick")
                .setDateOfBirth(Date.valueOf("2010-10-10"))
                .setGrade(new Performance(3L, "уд"))
                .build();

        studentRepo.add(student)
                .log()
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    @DisplayName("Get student by Id")
    void getStudentById() {
        studentRepo.getById(1L)
                .as(StepVerifier::create)
                .consumeNextWith(s -> {
                    Assertions.assertThat(s.getFullName()).isEqualTo("Иванов Иван Иванович");
                    Assertions.assertThat(s.getDateOfBirth().toString()).isEqualTo("2010-06-12");
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Try to get student by wrong Id, should throw exception")
    void getStudentById_withException() {
        studentRepo.getById(100_000_000_000L)
                .as(StepVerifier::create)
                .expectError(StudentNotFoundException.class)
                .log()
                .verify();
    }
}
