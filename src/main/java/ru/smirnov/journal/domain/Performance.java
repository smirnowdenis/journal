package ru.smirnov.journal.domain;

import org.springframework.data.annotation.Id;

import java.util.Map;

public class Performance {
    @Id
    private Long id;
    private String grade;

    public Performance(Long id, String grade) {
        this.id = id;
        this.grade = grade;
    }

    public Long getId() {
        return id;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public static Performance gradeFromRow(Map<String, Object> row) {
        if (row.get("p_id") != null) {
            return new Performance(Long.parseLong(row.get("p_id").toString()), row.get("grade").toString());
        } else {
            throw new IllegalStateException(String.format("Student with ID: %s didn't have link with grade", row.get("s_id")));
        }
    }
}
