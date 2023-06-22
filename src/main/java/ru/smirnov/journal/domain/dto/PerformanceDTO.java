package ru.smirnov.journal.domain.dto;

import jakarta.validation.constraints.*;

public class PerformanceDTO {
    @NotNull(message = "Id shouldn't be null")
    @Positive(message = "Id should be positive")
    @Min(value = 2, message = "Grade id should be equals or greater than 2")
    @Max(value = 5, message = "Grade id should be equals or lesser than 5")
    private Long id;
    @NotBlank(message = "Grade is mandatory")
    private String grade;

    public PerformanceDTO(Long id, String grade) {
        this.id = id;
        this.grade = grade;
    }

    public PerformanceDTO(String grade) {
        this.grade = grade;
    }

    public PerformanceDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
