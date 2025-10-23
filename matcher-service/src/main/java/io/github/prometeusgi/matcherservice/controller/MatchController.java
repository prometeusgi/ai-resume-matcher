package io.github.prometeusgi.matcherservice.controller;

import io.github.prometeusgi.matcherservice.client.AiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/match")
@RequiredArgsConstructor
public class MatchController {

    private final AiClient aiClient;

    @PostMapping
    public AiClient.AiResponse match(@RequestBody AiClient.AiRequest request) {
        return aiClient.analyze(request.getResume(), request.getJobDescription());
    }
}