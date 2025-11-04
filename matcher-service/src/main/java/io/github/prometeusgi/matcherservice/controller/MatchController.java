package io.github.prometeusgi.matcherservice.controller;

import io.github.prometeusgi.matcherservice.domain.MatchResult;
import io.github.prometeusgi.matcherservice.service.MatchingService;
import io.github.prometeusgi.matcherservice.web.dto.MatchRequest;
import io.github.prometeusgi.matcherservice.web.dto.MatchResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchingService matchingService;

    @PostMapping
    public MatchResponse match(@Valid @RequestBody MatchRequest request) {
        return matchingService.matchAndPersist(request);
    }

    @GetMapping
    public List<MatchResult> findAll() {
        return matchingService.findAll();
    }
}
