package io.github.prometeusgi.matcherservice.client;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AiClient {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String baseUrl = "http://localhost:8000/match";

    public AiResponse analyze(String resume, String jobDescription) {
        AiRequest req = new AiRequest(resume, jobDescription);
        return restTemplate.postForObject(baseUrl, req, AiResponse.class);
    }

    @Data
    public static class AiRequest {
        private final String resume;
        private final String jobDescription;
    }

    @Data
    public static class AiResponse {
        private double matchScore;
        private String summary;
        private String[] skillsMatched;
        private String[] skillsMissing;
    }
}