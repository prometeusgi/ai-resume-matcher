package io.github.prometeusgi.matcherservice.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class AiResponse {
    @JsonProperty("match_score")
    private double matchScore;

    @JsonProperty("summary")
    private String summary;

    @JsonProperty("skills_matched")
    private List<String> skillsMatched;

    @JsonProperty("skills_missing")
    private List<String> skillsMissing;
}
