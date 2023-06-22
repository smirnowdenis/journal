package ru.smirnov.journal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.smirnov.journal.domain.Performance;
import ru.smirnov.journal.domain.Student;
import ru.smirnov.journal.domain.dto.StudentDTO;
import ru.smirnov.journal.repo.StudentRepo;

import java.sql.Date;

@Service
public class StudentService {
    public static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepo studentRepo;

    private final PerformanceService performanceService;

    public StudentService(StudentRepo studentRepo, PerformanceService performanceService) {
        this.studentRepo = studentRepo;
        this.performanceService = performanceService;
    }

    public Flux<Student> getAllStudent() {
        return studentRepo.getAllStudents();
    }

    public Mono<Student> getStudentById(Long id) {
        return studentRepo.getById(id);
    }

    public Mono<Void> add(StudentDTO studentDTO) {
        performanceService.getPerformanceById(studentDTO.getGradeId())
                .subscribe(p -> {
                    Student student = Student.builder()
                            .setFullName(studentDTO.getFullName())
                            .setDateOfBirth(Date.valueOf(studentDTO.getDateOfBirth()))
                            .setGrade(new Performance(p.getId(), p.getGrade()))
                            .build();
                    studentRepo.add(student);
                });
        return Mono.empty();
    }

    public Mono<Void> delete(Long id) {
        getStudentById(id).subscribe(student -> studentRepo.delete(student.getId()));
        return Mono.empty();
    }

    public Mono<Void> update(StudentDTO studentDTO) {
        performanceService.getPerformanceById(studentDTO.getGradeId())
                .subscribe(p -> getStudentById(studentDTO.getId())
                        .subscribe(s -> {
                            Student student = Student.builder()
                                    .setId(studentDTO.getId())
                                    .setFullName(studentDTO.getFullName())
                                    .setDateOfBirth(Date.valueOf(studentDTO.getDateOfBirth()))
                                    .setGrade(new Performance(p.getId(), p.getGrade()))
                                    .build();
                            studentRepo.update(student);
                        }));
        return Mono.empty();
    }
}
