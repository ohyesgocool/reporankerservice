package org.git.reporankerservice.client;

import org.git.reporankerservice.model.github.GitHubSearchResponse;
import reactor.core.publisher.Mono;
import java.time.LocalDate;

public interface GitHubClient {
    Mono<GitHubSearchResponse> searchRepositories(LocalDate createdFrom, String language, int page, int size);
}