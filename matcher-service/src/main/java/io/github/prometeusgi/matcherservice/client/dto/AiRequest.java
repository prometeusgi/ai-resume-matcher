package io.github.prometeusgi.matcherservice.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AiRequest {
    @NotBlank
    @JsonProperty("resume")
    private String resume;

    @NotBlank
    @JsonProperty("job_description")
    private String jobDescription;
}
