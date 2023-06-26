package ru.smirnov.journal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.result.view.Rendering;
import org.thymeleaf.spring6.context.webflux.IReactiveDataDriverContextVariable;
import org.thymeleaf.spring6.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Mono;
import ru.smirnov.journal.domain.dto.PerformanceDTO;
import ru.smirnov.journal.service.PerformanceService;
import ru.smirnov.journal.util.ValidateParams;

import java.util.Arrays;
import java.util.List;

@Tag(name = "Performance controller", description = "Controller for management performance, allow to edit grades ")
@Controller
public class PerformanceController {
    public static final Logger logger = LoggerFactory.getLogger(PerformanceController.class);

    private final PerformanceService performanceService;

    public PerformanceController(PerformanceService performanceService) {
        this.performanceService = performanceService;
    }

    @Operation(summary = "Get all performances",
            description = "Get all performances and return page with them",
            tags = {"performances", "get"})
    @ApiResponse(responseCode = "200", description = "success operation")
    @ApiResponse(responseCode = "500", description = "something went wrong on server side, call to sysadmins")
    @GetMapping("/performance")
    public Mono<Rendering> getAllPerformances() {
        IReactiveDataDriverContextVariable reactiveDataDrivenMode =
                new ReactiveDataDriverContextVariable(performanceService.getAllPerformances()
                        .doOnComplete(() -> logger.info("Successfully have got lif of all performances")));
        return Mono.just(Rendering.view("performance")
                .modelAttribute("performances", reactiveDataDrivenMode)
                .status(HttpStatus.OK)
                .build());
    }

    @Operation(summary = "Update performance",
            description = "Update performance and return page with them",
            tags = {"performances", "post", "update"})
    @ApiResponse(responseCode = "200", description = "success operation")
    @ApiResponse(responseCode = "400", description = "check that you enter correct id and grade")
    @PostMapping(value = "/performance/update", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Mono<Rendering> updatePerformance(@ModelAttribute PerformanceDTO performanceDTO, Model model) {
        logger.info("Started updating performance with ID: {}", performanceDTO.getId());

        List<String> errors = ValidateParams.handleErrorParams(performanceDTO, Arrays.asList("id", "grade"));
        if (!errors.isEmpty()) {
            return Mono.just(Rendering.view("error")
                    .modelAttribute("errors", errors)
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }

        performanceService.update(performanceDTO);
        return Mono.just(Rendering.redirectTo("/performance")
                .status(HttpStatus.OK)
                .build());
    }

    @ModelAttribute("updatePerformance")
    public PerformanceDTO modelAttributeUpdatePerformance() {
        return new PerformanceDTO();
    }
}
