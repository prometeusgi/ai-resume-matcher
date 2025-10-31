package io.github.prometeusgi.matcherservice.config;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties(prefix = "ai.service")
public class AiServiceProperties {
    @NotBlank
    private String baseUrl = "http://localhost:8000";

    @Min(1)
    private int connectTimeoutMs = 2000;

    @Min(1)
    private int readTimeoutMs = 5000;
}

