package ru.smirnov.journal.repo;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import ru.smirnov.journal.domain.Performance;

public interface PerformanceRepo extends ReactiveCrudRepository<Performance, Long> {
}
