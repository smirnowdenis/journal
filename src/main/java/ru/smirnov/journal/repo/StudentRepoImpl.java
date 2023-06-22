package ru.smirnov.journal.repo;

import io.r2dbc.spi.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.smirnov.journal.domain.Student;
import ru.smirnov.journal.exception.StudentNotFoundException;

import java.sql.Date;
import java.time.LocalDate;

@Component
public class StudentRepoImpl implements StudentRepo {
    public static final Logger logger = LoggerFactory.getLogger(StudentRepoImpl.class);

    private static final String SELECT_QUERY = """
                SELECT s.id s_id, full_name, date_of_birth, performance_id,
                p.id p_id, grade FROM journal.student s
                LEFT JOIN journal.performance p ON p.id = s.performance_id
            """;

    private final DatabaseClient client;

    public StudentRepoImpl(ConnectionFactory connectionFactory) {
        this.client = DatabaseClient.create(connectionFactory);
    }

    @Override
    public Flux<Student> getAllStudents() {
        String query = String.format("%s ORDER BY s_id", SELECT_QUERY);
        return client.sql(query)
                .fetch()
                .all()
                .bufferUntilChanged(res -> res.get("s_id"))
                .flatMap(Student::fromRows)
                .doOnError(e -> logger.info("Error while getting list of students: {}", e.getMessage()));
    }

    @Override
    public Mono<Student> getById(Long id) {
        String query = String.format("%s WHERE s.id = :id", SELECT_QUERY);
        return client.sql(query)
                .bind("id", id)
                .fetch()
                .all()
                .bufferUntilChanged(res -> res.get("s_id"))
                .flatMap(Student::fromRows)
                .singleOrEmpty()
                .switchIfEmpty(Mono.error(new StudentNotFoundException(id)));
    }

    @Override
    public Mono<Void> add(Student student) {
        int[] date = splitDateByYearMonthDay(student.getDateOfBirth());
        client.sql("INSERT INTO journal.student(full_name, date_of_birth, performance_id) " +
                        "VALUES ((:fullName), (:dateOfBirth), (:performanceId))")
                .bind("fullName", student.getFullName())
                .bind("dateOfBirth", LocalDate.of(date[0], date[1], date[2]))
                .bind("performanceId", student.getGrade().getId())
                .filter(((statement, next) -> statement.returnGeneratedValues("id").execute()))
                .fetch()
                .first()
                .doOnSuccess(res -> logger.info("Student with ID: {} was added", Long.parseLong(res.get("id").toString())))
                .doOnError(e -> logger.info("Error while adding student with full name: {}, Error message: {}", student.getFullName(), e.getMessage()))
                .subscribe();
        return Mono.empty();
    }

    @Override
    public Mono<Void> delete(Long id) {
        client.sql("DELETE FROM journal.student WHERE id=:id")
                .bind("id", id)
                .fetch()
                .rowsUpdated()
                .doOnSuccess(res -> logger.info("Student with ID: {} was deleted", id))
                .doOnError(e -> logger.info("Error while deleting student with ID: {}, Error message: {}", id, e.getMessage()))
                .subscribe();
        return Mono.empty();
    }

    @Override
    public Mono<Void> update(Student student) {
        int[] date = splitDateByYearMonthDay(student.getDateOfBirth());
        client.sql("UPDATE journal.student " +
                        "SET full_name = :fullName, date_of_birth = :dateOfBirth, performance_id = :perfromanceId " +
                        "WHERE id = :id")
                .bind("id", student.getId())
                .bind("fullName", student.getFullName())
                .bind("dateOfBirth", LocalDate.of(date[0], date[1], date[2]))
                .bind("perfromanceId", student.getGrade().getId())
                .fetch()
                .rowsUpdated()
                .doOnSuccess(res -> logger.info("Student with ID: {} was updated", student.getId()))
                .doOnError(e -> logger.info("Error while updating student with ID: {}, Error message: {}", student.getId(), e.getMessage()))
                .subscribe();
        return Mono.empty();
    }

    private int[] splitDateByYearMonthDay(Date date) {
        String[] split = date.toString().split("-");
        return new int[]{Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2])};
    }
}
