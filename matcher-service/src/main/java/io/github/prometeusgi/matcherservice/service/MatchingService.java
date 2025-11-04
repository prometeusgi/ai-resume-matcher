package io.github.prometeusgi.matcherservice.service;

import io.github.prometeusgi.matcherservice.client.AiClient;
import io.github.prometeusgi.matcherservice.client.dto.AiRequest;
import io.github.prometeusgi.matcherservice.client.dto.AiResponse;
import io.github.prometeusgi.matcherservice.domain.MatchResult;
import io.github.prometeusgi.matcherservice.repository.MatchResultRepository;
import io.github.prometeusgi.matcherservice.web.dto.MatchRequest;
import io.github.prometeusgi.matcherservice.web.dto.MatchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchingService {

    private final AiClient aiClient;
    private final MatchResultRepository matchResultRepository;

    public MatchResponse matchAndPersist(MatchRequest request) {
        AiRequest aiRequest = new AiRequest(request.getResume(), request.getJobDescription());
        AiResponse response = aiClient.analyze(aiRequest.getResume(), aiRequest.getJobDescription());
        saveResult(request, response);
        return toMatchResponse(response);
    }

    public List<MatchResult> findAll() {
        return matchResultRepository.findAll();
    }

    private void saveResult(MatchRequest request, AiResponse response) {
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
    }

    private static MatchResponse toMatchResponse(AiResponse response) {
        MatchResponse out = new MatchResponse();
        out.setMatchScore(response.getMatchScore());
        out.setSummary(response.getSummary());
        out.setSkillsMatched(response.getSkillsMatched());
        out.setSkillsMissing(response.getSkillsMissing());
        return out;
    }
}
