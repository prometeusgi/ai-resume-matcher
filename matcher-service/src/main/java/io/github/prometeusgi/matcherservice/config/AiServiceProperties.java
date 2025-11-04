package io.github.prometeusgi.matcherservice.config;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.hibernate.validator.constraints.time.DurationMin;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@Validated
@ConfigurationProperties(prefix = "ai.service")
public record AiServiceProperties(
        @NotBlank @URL String baseUrl,
        @DurationMin(millis = 1) Duration connectTimeout,
        @DurationMin(millis = 1) Duration readTimeout
) {
}
