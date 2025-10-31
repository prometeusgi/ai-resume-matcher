package io.github.prometeusgi.matcherservice.client;

import io.github.prometeusgi.matcherservice.client.dto.AiResponse;

public interface AiClient {
    AiResponse analyze(String resume, String jobDescription);
}

