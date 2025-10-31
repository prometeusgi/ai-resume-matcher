package io.github.prometeusgi.matcherservice.controller;

import io.github.prometeusgi.matcherservice.client.dto.AiRequest;
import io.github.prometeusgi.matcherservice.client.dto.AiResponse;
import io.github.prometeusgi.matcherservice.domain.MatchResult;
import io.github.prometeusgi.matcherservice.repo.MatchResultRepository;
import io.github.prometeusgi.matcherservice.service.MatchingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/match")
@RequiredArgsConstructor
public class MatchController {

    private final MatchingService matchingService;
    private final MatchResultRepository matchResultRepository;

    @PostMapping
    public AiResponse match(@Valid @RequestBody AiRequest request) {
        return matchingService.matchAndPersist(request);
    }

    @GetMapping("/all")
    public List<MatchResult> findAll() {
        return matchResultRepository.findAll();
    }
}
