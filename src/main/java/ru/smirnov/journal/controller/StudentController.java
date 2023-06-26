package ru.smirnov.journal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.result.view.Rendering;
import org.thymeleaf.spring6.context.webflux.IReactiveDataDriverContextVariable;
import org.thymeleaf.spring6.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Mono;
import ru.smirnov.journal.domain.dto.StudentDTO;
import ru.smirnov.journal.service.StudentService;
import ru.smirnov.journal.util.ValidateParams;

import java.util.Arrays;
import java.util.List;

@Tag(name = "Student controller", description = "Controller for management students (crud operations)")
@Controller
public class StudentController {
    public static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @Operation(summary = "Get all students",
            description = "Get all students and return main page of application",
            tags = {"students", "get"})
    @ApiResponse(responseCode = "200", description = "success operation")
    @ApiResponse(responseCode = "500", description = "something went wrong on server side, call to sysadmins")
    @GetMapping("/")
    public Mono<Rendering> getAllStudents() {
        IReactiveDataDriverContextVariable reactiveDataDrivenMode =
                new ReactiveDataDriverContextVariable(studentService.getAllStudent()
                        .doOnComplete((() -> logger.info("Successfully have got list of all students"))));
        return Mono.just(Rendering.view("index")
                .modelAttribute("students", reactiveDataDrivenMode)
                .status(HttpStatus.OK)
                .build());
    }

    @Operation(summary = "Add student",
            description = "Add student and redirect to index.html",
            tags = {"students", "post", "create"})
    @ApiResponse(responseCode = "200", description = "student was added")
    @ApiResponse(responseCode = "400", description = "check that you enter correct full name and date of birth")
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Mono<Rendering> addStudent(@ModelAttribute StudentDTO studentDTO) {
        logger.info("Started adding user with full name: {}", studentDTO.getFullName());

        List<String> errors = ValidateParams.handleErrorParams(studentDTO, Arrays.asList("fullName", "dateOfBirth", "gradeId"));
        if (!errors.isEmpty()) {
            return Mono.just(Rendering.view("error")
                    .modelAttribute("errors", errors)
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }
        studentService.add(studentDTO);
        return Mono.just(Rendering.redirectTo("/").status(HttpStatus.CREATED).build());
    }

    @Operation(summary = "Delete student",
            description = "Delete student and redirect to index.html",
            tags = {"students", "post", "delete"})
    @ApiResponse(responseCode = "200", description = "student was deleted")
    @ApiResponse(responseCode = "400", description = "check that you enter correct id")
    @PostMapping(value = "/delete", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Mono<Rendering> deleteStudent(@ModelAttribute StudentDTO studentDTO) {
        logger.info("Started deleting user with ID: {}", studentDTO.getId());

        List<String> errors = ValidateParams.handleErrorParams(studentDTO, Arrays.asList("id"));
        if (!errors.isEmpty()) {
            return Mono.just(Rendering.view("error")
                    .modelAttribute("errors", errors)
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }

        studentService.delete(studentDTO.getId());
        return Mono.just(Rendering.redirectTo("/").status(HttpStatus.OK).build());
    }

    @Operation(summary = "Update student",
            description = "Update student and redirect to index.html",
            tags = {"students", "post", "update"})
    @ApiResponse(responseCode = "200", description = "student was updated")
    @ApiResponse(responseCode = "400", description = "check that you enter correct id, full name, date of birth")
    @PostMapping(value = "/update", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Mono<Rendering> updateStudent(@ModelAttribute StudentDTO studentDTO) {
        logger.info("Started updating user with ID: {}", studentDTO.getId());

        List<String> errors = ValidateParams.handleErrorParams(studentDTO, Arrays.asList("id", "fullName", "dateOfBirth", "gradeId"));
        if (!errors.isEmpty()) {
            return Mono.just(Rendering.view("error")
                    .modelAttribute("errors", errors)
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }
        studentService.update(studentDTO);
        return Mono.just(Rendering.redirectTo("/").status(HttpStatus.OK).build());
    }

    @ModelAttribute("addStudent")
    public StudentDTO modelAttributeAddStudent() {
        return new StudentDTO();
    }

    @ModelAttribute("deleteStudent")
    public StudentDTO modelAttributeDeleteStudent() {
        return new StudentDTO();
    }

    @ModelAttribute("updateStudent")
    public StudentDTO modelAttributeUpdateStudent() {
        return new StudentDTO();
    }
}
