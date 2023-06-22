package ru.smirnov.journal.repo;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.smirnov.journal.domain.Student;

public interface StudentRepo {
    Flux<Student> getAllStudents();

    Mono<Student> getById(Long id);

    Mono<Void> add(Student student);

    Mono<Void> delete(Long id);

    Mono<Void> update(Student student);
}
