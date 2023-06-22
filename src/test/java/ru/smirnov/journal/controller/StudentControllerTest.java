package ru.smirnov.journal.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.smirnov.journal.domain.Performance;
import ru.smirnov.journal.domain.Student;
import ru.smirnov.journal.service.StudentService;

import java.nio.charset.StandardCharsets;
import java.sql.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = StudentController.class)
class StudentControllerTest {

    @Autowired
    private WebTestClient client;

    @MockBean
    private StudentService studentService;


    @Test
    @DisplayName("GET / - should return 1 student")
    void getAllStudents() {
        when(studentService.getAllStudent()).thenReturn(Flux.just(stubbedStudent()));

        client.get()
                .uri("/")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(res ->
                        Assertions.assertThat(new String(res.getResponseBody(), StandardCharsets.UTF_8))
                                .contains("Иванов Иван").contains("2010-10-15").contains("отл"));
    }

    @Test
    @DisplayName("POST /add - should show validation error")
    void addStudent() {
        client.post().uri("/add")
                .body(BodyInserters.fromFormData("fullName", "Nick")
                        .with("dateOfBirth", "2010-10-10")
                        .with("gradeId", String.valueOf(3L)))
                .exchange()
                .expectBody()
                .consumeWith(res -> Assertions.assertThat(new String(res.getResponseBody(), StandardCharsets.UTF_8)
                        .contains("Full name should be like")));
    }

    @Test
    @DisplayName("POST /delete - should show validation error")
    void deleteStudent() {
        when(studentService.delete(any())).thenReturn(Mono.empty());

        client.post().uri("/delete")
                .body(BodyInserters.fromFormData("id", String.valueOf(-11)))
                .exchange()
                .expectBody()
                .consumeWith(res -> Assertions.assertThat(new String(res.getResponseBody(), StandardCharsets.UTF_8)
                        .contains("Id should be positive")));
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