package io.github.prometeusgi.matcherservice.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@Document(collection = "match_results")
public class MatchResult {
    @Id
    private String id;
    private String candidateName;
    private String resumeText;
    private String jobTitle;
    private String jobDescription;
    private double matchScore;
    private List<String> skillsMatched;
    private List<String> skillsMissing;
    private String summary;

    @CreatedDate
    private Instant createdAt;
}
