package io.github.prometeusgi.matcherservice.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchRequest {
    @NotBlank
    private String resume;

    @NotBlank
    private String jobDescription;
}

