package org.git.reporankerservice.service;

import org.git.reporankerservice.client.GitHubClient;
import org.git.reporankerservice.model.Repository;
import org.git.reporankerservice.model.RepositoryRequest;
import org.git.reporankerservice.model.RepositoryResponse;
import org.git.reporankerservice.model.github.GitHubRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class RepositoryServiceImpl implements RepositoryService {

    private static final Logger logger = LoggerFactory.getLogger(RepositoryServiceImpl.class);
    private static final int MAX_RECENCY_SCORE = 100;

    private final GitHubClient gitHubClient;

    public RepositoryServiceImpl(GitHubClient gitHubClient) {
        this.gitHubClient = gitHubClient;
    }

    @Override
    @Cacheable(value = "repositories", key = "#request")
    public CompletableFuture<RepositoryResponse> getRepositories(RepositoryRequest request) {
        logger.info("Fetching repositories for: {}", request);

        return gitHubClient.searchRepositories(
                        request.createdFrom(),
                        request.language(),
                        request.page(),
                        request.size()
                )
                .flatMap(response -> {
                    if (response == null || response.items() == null) {
                        logger.warn("Received null or empty response from GitHub client for request: {}", request);
                        return Mono.just(new RepositoryResponse(Collections.emptyList()));
                    }
                    List<Repository> rankedRepos = response.items().stream()
                            .map(this::mapAndScore)
                            .toList();
                    logger.info("Successfully fetched and ranked {} repositories for request: {}", rankedRepos.size(), request);
                    return Mono.just(new RepositoryResponse(rankedRepos));
                })
                .onErrorResume(e -> {
                    logger.error("An error occurred while fetching repositories for request: {}", request, e);
                    return Mono.just(new RepositoryResponse(Collections.emptyList()));
                })
                .toFuture();
    }

    private Repository mapAndScore(GitHubRepository raw) {
        long score = calculateScore(raw.stars(), raw.forks(), raw.pushedAt());
        return new Repository(
                raw.name(),
                raw.owner().login(),
                raw.stars(),
                raw.forks(),
                raw.pushedAt(),
                score
        );
    }

    private long calculateScore(int stars, int forks, OffsetDateTime lastPush) {
        long daysSincePush = ChronoUnit.DAYS.between(lastPush, OffsetDateTime.now());
        long recencyScore = Math.max(0, MAX_RECENCY_SCORE - daysSincePush);
        return stars + forks + recencyScore;
    }
}