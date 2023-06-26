package ru.smirnov.journal.domain.dto;

import jakarta.validation.constraints.*;

public class StudentDTO {
    @Positive(message = "Id should be positive")
    @NotNull(message = "Id shouldn't be null")
    private Long id;
    @NotBlank(message = "Full name is mandatory")
    @Pattern(regexp = "[а-яА-Я]+\\s[а-яА-Я]+\\s[а-яА-Я]+|[а-яА-Я]+\\s[а-яА-Я]+|[a-zA-Z]+\\s[a-zA-Z]+\\s[a-zA-Z]+|[a-zA-Z]+\\s[a-zA-Z]+",
            message = "Full name should be like \"FirstName SecondName SurName\" or \"FirstName SecondName\"")
    private String fullName;
    @NotBlank(message = "Date of birth is mandatory")
    @Pattern(regexp = "[1-2]{1}[0-9]{3}-[0-1]{1}[0-9]{1}-[0-3]{1}[0-9]{1}",
            message = "Date should be like \"YYYY-MM-DD\"")
    private String dateOfBirth;
    @Min(value = 2, message = "Grade should be equals or greater than 2")
    @Max(value = 5, message = "Grade should be equals or lesser than 5")
    private Long gradeId;

    public StudentDTO() {
    }

    public StudentDTO(Long id) {
        this.id = id;
    }

    public StudentDTO(String fullName, String dateOfBirth, Long gradeId) {
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.gradeId = gradeId;
    }

    public StudentDTO(Long id, String fullName, String dateOfBirth, Long gradeId) {
        this.id = id;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.gradeId = gradeId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Long getGradeId() {
        return gradeId;
    }

    public void setGradeId(Long gradeId) {
        this.gradeId = gradeId;
    }
}
