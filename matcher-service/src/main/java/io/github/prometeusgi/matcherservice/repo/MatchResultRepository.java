package io.github.prometeusgi.matcherservice.repo;

import io.github.prometeusgi.matcherservice.domain.MatchResult;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchResultRepository extends MongoRepository<MatchResult, String> {}
