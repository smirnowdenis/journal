package ru.smirnov.journal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.smirnov.journal.domain.Performance;
import ru.smirnov.journal.domain.dto.PerformanceDTO;
import ru.smirnov.journal.exception.PerformanceNotFoundException;
import ru.smirnov.journal.repo.PerformanceRepo;

import java.util.Comparator;

@Service
public class PerformanceService {
    public static final Logger logger = LoggerFactory.getLogger(PerformanceService.class);

    private final PerformanceRepo performanceRepo;

    public PerformanceService(PerformanceRepo performanceRepo) {
        this.performanceRepo = performanceRepo;
    }

    public Flux<Performance> getAllPerformances() {
        return performanceRepo.findAll()
                .sort(Comparator.comparing(Performance::getId))
                .doOnError(e -> logger.error("Error while getting all performances: {}", e.getMessage()));
    }

    public Mono<Performance> getPerformanceById(Long id) {
        if (id == null) return Mono.just(new Performance(null, null));
        return performanceRepo.findById(id)
                .switchIfEmpty(Mono.error(new PerformanceNotFoundException(id)));
    }

    public Mono<Void> update(PerformanceDTO performanceDTO) {
        getPerformanceById(performanceDTO.getId())
                .subscribe(p -> performanceRepo.save(new Performance(performanceDTO.getId(), performanceDTO.getGrade()))
                        .doOnSuccess(res -> logger.info("Performance with ID: {} was updated", performanceDTO.getId()))
                        .subscribe());
        return Mono.empty();
    }
}
