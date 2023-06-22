package ru.smirnov.journal.domain;

import reactor.core.publisher.Mono;

import java.sql.Date;
import java.util.List;
import java.util.Map;

public class Student {
    private Long id;
    private String fullName;
    private Date dateOfBirth;
    private Performance grade;

    private Student() {
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public Performance getGrade() {
        return grade;
    }

    public static Mono<Student> fromRows(List<Map<String, Object>> rows) {
        return Mono.just(Student.builder()
                .setId((Long.parseLong(rows.get(0).get("s_id").toString())))
                .setFullName(rows.get(0).get("full_name").toString())
                .setDateOfBirth(Date.valueOf(rows.get(0).get("date_of_birth").toString()))
                .setGrade(Performance.gradeFromRow(rows.get(0)))
                .build());
    }

    public static Builder builder() {
        return new Student().new Builder();
    }

    public class Builder {
        private Builder() {
        }

        public Builder setId(Long id) {
            Student.this.id = id;
            return this;
        }

        public Builder setFullName(String fullName) {
            Student.this.fullName = fullName;
            return this;
        }

        public Builder setDateOfBirth(Date dateOfBirth) {
            Student.this.dateOfBirth = dateOfBirth;
            return this;
        }

        public Builder setGrade(Performance grade) {
            Student.this.grade = grade;
            return this;
        }

        public Student build() {
            return Student.this;
        }
    }
}
