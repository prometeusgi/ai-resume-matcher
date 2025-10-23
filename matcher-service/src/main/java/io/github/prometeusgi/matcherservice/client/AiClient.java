package io.github.prometeusgi.matcherservice.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class AiClient {

    private final WebClient webClient;

    public AiClient(WebClient aiWebClient) {
        this.webClient = aiWebClient;
    }

    public AiResponse analyze(String resume, String jobDescription) {
        AiRequest request = new AiRequest(resume, jobDescription);

        return webClient.post()
                .uri("/match")
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(
                                        new RuntimeException("AI service error: " + errorBody))
                                )
                )
                .bodyToMono(AiResponse.class)
                .block();

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AiRequest {
        @JsonProperty("resume")
        private String resume;

        @JsonProperty("job_description")
        private String jobDescription;
    }

    @Data
    public static class AiResponse {
        @JsonProperty("match_score")
        private double matchScore;

        @JsonProperty("summary")
        private String summary;

        @JsonProperty("skills_matched")
        private String[] skillsMatched;

        @JsonProperty("skills_missing")
        private String[] skillsMissing;
    }
}
