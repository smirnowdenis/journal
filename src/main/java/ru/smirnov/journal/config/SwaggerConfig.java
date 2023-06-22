package ru.smirnov.journal.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title="Journal Application", version = "0.1",
        description = "Spring boot 3.1, Webflux, Postgres, Flyway"))
public class SwaggerConfig {
}
