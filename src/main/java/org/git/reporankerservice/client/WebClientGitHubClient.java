package org.git.reporankerservice.client;

import org.git.reporankerservice.model.Language;
import org.git.reporankerservice.model.github.GitHubSearchResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class WebClientGitHubClient implements GitHubClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebClientGitHubClient.class);
    private final WebClient githubWebClient;

    public WebClientGitHubClient(WebClient githubWebClient) {
        this.githubWebClient = githubWebClient;
    }

    @Override
    public Mono<GitHubSearchResponse> searchRepositories(LocalDate createdFrom, String language, int page, int size) {
        if (!Language.isValid(language)) {
            LOGGER.warn("Invalid language provided: {}", language);
            return Mono.just(new GitHubSearchResponse(java.util.Collections.emptyList()));
        }

        String query = String.format("created:>%s language:%s",
                createdFrom.format(DateTimeFormatter.ISO_LOCAL_DATE),
                language);

        LOGGER.info("Querying GitHub with: {}", query);

        return githubWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search/repositories")
                        .queryParam("q", query)
                        .queryParam("sort", "stars")
                        .queryParam("order", "desc")
                        .queryParam("page", page)
                        .queryParam("per_page", size)
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse ->
                        clientResponse.bodyToMono(String.class)
                                .defaultIfEmpty("")
                                .flatMap(body -> {
                                    LOGGER.error("Error from GitHub API: status code {} and body {}", clientResponse.statusCode(), body);
                                    return Mono.error(new RuntimeException("Error from GitHub API. Status: " + clientResponse.statusCode()));
                                }))
                .bodyToMono(GitHubSearchResponse.class)
                .onErrorResume(e -> {
                    LOGGER.error("Error while searching repositories", e);
                    return Mono.just(new GitHubSearchResponse(java.util.Collections.emptyList()));
                });
    }
}
