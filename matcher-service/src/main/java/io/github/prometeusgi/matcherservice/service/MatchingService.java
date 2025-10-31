package io.github.prometeusgi.matcherservice.service;

import io.github.prometeusgi.matcherservice.client.AiClient;
import io.github.prometeusgi.matcherservice.client.dto.AiRequest;
import io.github.prometeusgi.matcherservice.client.dto.AiResponse;
import io.github.prometeusgi.matcherservice.domain.MatchResult;
import io.github.prometeusgi.matcherservice.repo.MatchResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchingService {

    private final AiClient aiClient;
    private final MatchResultRepository matchResultRepository;

    public AiResponse matchAndPersist(AiRequest request) {
        AiResponse response = aiClient.analyze(request.getResume(), request.getJobDescription());

        MatchResult result = new MatchResult();
        result.setResumeText(request.getResume());
        result.setJobDescription(request.getJobDescription());
        result.setMatchScore(response.getMatchScore());
        result.setSummary(response.getSummary());

        List<String> matched = response.getSkillsMatched() != null
                ? response.getSkillsMatched()
                : Collections.emptyList();
        List<String> missing = response.getSkillsMissing() != null
                ? response.getSkillsMissing()
                : Collections.emptyList();
        result.setSkillsMatched(matched);
        result.setSkillsMissing(missing);

        matchResultRepository.save(result);
        return response;
    }
}
