package io.github.prometeusgi.matcherservice.client;

import io.github.prometeusgi.matcherservice.client.dto.AiRequest;
import io.github.prometeusgi.matcherservice.client.dto.AiResponse;
import io.github.prometeusgi.matcherservice.client.exception.AiServiceException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

@Component
public class DefaultAiClient implements AiClient {

    private final RestClient restClient;

    public DefaultAiClient(RestClient aiRestClient) {
        this.restClient = aiRestClient;
    }

    @Override
    public AiResponse analyze(String resume, String jobDescription) {
        AiRequest request = new AiRequest(resume, jobDescription);
        try {
            return restClient.post()
                    .uri("/match")
                    .body(request)
                    .retrieve()
                    .body(AiResponse.class);
        } catch (RestClientResponseException ex) {
            String msg = "AI service error: " + ex.getStatusCode().value() + ": " + ex.getResponseBodyAsString();
            throw new AiServiceException(msg, ex);
        } catch (RestClientException ex) {
            throw new AiServiceException("AI service call failed", ex);
        }
    }
}
