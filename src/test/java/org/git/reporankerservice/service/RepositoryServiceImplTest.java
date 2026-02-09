package org.git.reporankerservice.service;

import org.git.reporankerservice.client.GitHubClient;
import org.git.reporankerservice.model.RepositoryRequest;
import org.git.reporankerservice.model.RepositoryResponse;
import org.git.reporankerservice.model.github.GitHubOwner;
import org.git.reporankerservice.model.github.GitHubRepository;
import org.git.reporankerservice.model.github.GitHubSearchResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

class RepositoryServiceImplTest {

    @Mock
    private GitHubClient gitHubClient;

    private RepositoryServiceImpl repositoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        repositoryService = new RepositoryServiceImpl(gitHubClient);
    }

    @Test
    void getRepositories_shouldReturnRankedRepositories_whenSuccessful() throws ExecutionException, InterruptedException {
        // Given
        RepositoryRequest request = new RepositoryRequest(null, "java", 1, 10);
        GitHubRepository repo = new GitHubRepository("test-repo", new GitHubOwner("test-owner"), 100, 50, OffsetDateTime.now());
        GitHubSearchResponse searchResponse = new GitHubSearchResponse(List.of(repo));
        when(gitHubClient.searchRepositories(any(), any(), anyInt(), anyInt()))
                .thenReturn(Mono.just(searchResponse));

        // When
        CompletableFuture<RepositoryResponse> future = repositoryService.getRepositories(request);
        RepositoryResponse response = future.get();

        // Then
        assertEquals(1, response.repositories().size());
        assertEquals("test-repo", response.repositories().getFirst().name());
        assertTrue(response.repositories().getFirst().score() > 150); // stars + forks + recency
    }

    @Test
    void getRepositories_shouldReturnEmpty_whenClientReturnsEmpty() throws ExecutionException, InterruptedException {
        // Given
        RepositoryRequest request = new RepositoryRequest(null, "java", 1, 10);
        when(gitHubClient.searchRepositories(any(), any(), anyInt(), anyInt()))
                .thenReturn(Mono.just(new GitHubSearchResponse(Collections.emptyList())));

        // When
        CompletableFuture<RepositoryResponse> future = repositoryService.getRepositories(request);
        RepositoryResponse response = future.get();

        // Then
        assertTrue(response.repositories().isEmpty());
    }

    @Test
    void getRepositories_shouldReturnEmpty_whenClientErrors() throws ExecutionException, InterruptedException {
        // Given
        RepositoryRequest request = new RepositoryRequest(null, null, 1, 10);
        when(gitHubClient.searchRepositories(any(), any(), anyInt(), anyInt()))
                .thenReturn(Mono.error(new RuntimeException("Client Error")));

        // When
        CompletableFuture<RepositoryResponse> future = repositoryService.getRepositories(request);
        RepositoryResponse response = future.get();

        // Then
        assertTrue(response.repositories().isEmpty());
    }
}
