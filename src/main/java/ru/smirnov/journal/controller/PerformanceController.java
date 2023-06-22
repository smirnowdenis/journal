package ru.smirnov.journal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.thymeleaf.spring6.context.webflux.IReactiveDataDriverContextVariable;
import org.thymeleaf.spring6.context.webflux.ReactiveDataDriverContextVariable;
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
    @ApiResponse(responseCode = "200", description = "success operation",
            content = @Content(mediaType = "application/x-www-form-urlencoded",
                    schema = @Schema(implementation = String.class)))
    @ApiResponse(responseCode = "500", description = "something went wrong on server side, call to sysadmins")
    @GetMapping("/performance")
    public String getAllPerformances(final Model model) {
        IReactiveDataDriverContextVariable reactiveDataDrivenMode =
                new ReactiveDataDriverContextVariable(performanceService.getAllPerformances()
                        .doOnComplete(() -> logger.info("Successfully have got lif of all performances")));
        model.addAttribute("performances", reactiveDataDrivenMode);
        model.addAttribute("updatePerformance", new PerformanceDTO());
        return "performance";
    }

    @Operation(summary = "Update performance",
            description = "Update performance and return page with them",
            tags = {"performances", "post", "update"})
    @ApiResponse(responseCode = "200", description = "success operation",
            content = @Content(mediaType = "application/x-www-form-urlencoded",
                    schema = @Schema(implementation = String.class)))
    @ApiResponse(responseCode = "400", description = "check that you enter correct id and grade")
    @PostMapping("/performance/update")
    public String updatePerformance(@ModelAttribute PerformanceDTO performanceDTO, Model model) {
        logger.info("Started updating performance with ID: {}", performanceDTO.getId());

        List<String> errors = ValidateParams.handleErrorParams(performanceDTO, Arrays.asList("id", "grade"));
        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            return "error";
        }

        performanceService.update(performanceDTO);
        return "redirect:/performance";
    }
}
