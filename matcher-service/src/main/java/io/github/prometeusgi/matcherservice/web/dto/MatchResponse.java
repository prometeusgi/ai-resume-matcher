package io.github.prometeusgi.matcherservice.web.dto;

import lombok.Data;

import java.util.List;

@Data
public class MatchResponse {
    private double matchScore;
    private String summary;
    private List<String> skillsMatched;
    private List<String> skillsMissing;
}

