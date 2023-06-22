package ru.smirnov.journal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.thymeleaf.spring6.context.webflux.IReactiveDataDriverContextVariable;
import org.thymeleaf.spring6.context.webflux.ReactiveDataDriverContextVariable;
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
    @ApiResponse(responseCode = "200", description = "success operation",
            content = @Content(mediaType = "application/x-www-form-urlencoded",
                    schema = @Schema(implementation = String.class)))
    @ApiResponse(responseCode = "500", description = "something went wrong on server side, call to sysadmins")
    @GetMapping("/")
    public String getAllStudents(final Model model) {
        IReactiveDataDriverContextVariable reactiveDataDrivenMode =
                new ReactiveDataDriverContextVariable(studentService.getAllStudent()
                        .doOnComplete((() -> logger.info("Successfully have got list of all students"))));
        model.addAttribute("students", reactiveDataDrivenMode);
        model.addAttribute("addStudent", new StudentDTO());
        model.addAttribute("deleteStudent", new StudentDTO());
        model.addAttribute("updateStudent", new StudentDTO());
        return "index";
    }

    @Operation(summary = "Add student",
            description = "Add student and redirect to index.html",
            tags = {"students", "post", "create"})
    @ApiResponse(responseCode = "200", description = "student was added",
            content = @Content(mediaType = "application/x-www-form-urlencoded",
                    schema = @Schema(implementation = String.class)))
    @ApiResponse(responseCode = "400",
            description = "check that you enter correct full name, date of birth and grade id")
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public String addStudent(@ModelAttribute StudentDTO studentDTO, Model model) {
        logger.info("Started adding user with full name: {}", studentDTO.getFullName());

        List<String> errors = ValidateParams.handleErrorParams(studentDTO, Arrays.asList("fullName", "dateOfBirth", "gradeId"));
        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            return "error";
        }
        studentService.add(studentDTO);
        return "redirect:/";
    }

    @Operation(summary = "Delete student",
            description = "Delete student and redirect to index.html",
            tags = {"students", "post", "delete"})
    @ApiResponse(responseCode = "200", description = "student was deleted",
            content = @Content(mediaType = "application/x-www-form-urlencoded",
                    schema = @Schema(implementation = String.class)))
    @ApiResponse(responseCode = "400", description = "check that you enter correct id")
    @PostMapping("/delete")
    public String deleteStudent(@ModelAttribute StudentDTO studentDTO, Model model) {
        logger.info("Started deleting user with ID: {}", studentDTO.getId());

        List<String> errors = ValidateParams.handleErrorParams(studentDTO, Arrays.asList("id"));
        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            return "error";
        }
        studentService.delete(studentDTO.getId());
        return "redirect:/";
    }

    @Operation(summary = "Update student",
            description = "Update student and redirect to index.html",
            tags = {"students", "post", "update"})
    @ApiResponse(responseCode = "200", description = "student was updated",
            content = @Content(mediaType = "application/x-www-form-urlencoded",
                    schema = @Schema(implementation = String.class)))
    @ApiResponse(responseCode = "400", description = "check that you enter correct id, full name, date of birth, grade id")
    @PostMapping(value = "/update")
    public String updateStudent(@ModelAttribute StudentDTO studentDTO, Model model) {
        logger.info("Started updating user with ID: {}", studentDTO.getId());

        List<String> errors = ValidateParams.handleErrorParams(studentDTO, Arrays.asList("id", "fullName", "dateOfBirth", "gradeId"));
        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            return "error";
        }
        studentService.update(studentDTO);
        return "redirect:/";
    }
}
