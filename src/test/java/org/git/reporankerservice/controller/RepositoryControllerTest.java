package org.git.reporankerservice.controller;

import org.git.reporankerservice.model.RepositoryRequest;
import org.git.reporankerservice.model.RepositoryResponse;
import org.git.reporankerservice.service.RepositoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(RepositoryController.class)
class RepositoryControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private RepositoryService repositoryService;

    @Test
    void getRepositories_shouldReturnRepositories_whenSuccessful() {
        // Given
        RepositoryResponse expectedResponse = new RepositoryResponse(Collections.emptyList());
        when(repositoryService.getRepositories(any(RepositoryRequest.class)))
                .thenReturn(CompletableFuture.completedFuture(expectedResponse));

        // When & Then
        webTestClient.get()
                .uri("/api/v1/repositories/search?language=java")
                .exchange()
                .expectStatus().isOk()
                .expectBody(RepositoryResponse.class)
                .isEqualTo(expectedResponse);
    }

    @Test
    void getRepositories_shouldReturnEmpty_whenServiceThrowsException() {
        // Given
        when(repositoryService.getRepositories(any(RepositoryRequest.class)))
                .thenReturn(CompletableFuture.failedFuture(new RuntimeException("Service Error")));

        // When & Then
        webTestClient.get()
                .uri("/api/v1/repositories/search?language=java")
                .exchange()
                .expectStatus().isOk()
                .expectBody(RepositoryResponse.class)
                .isEqualTo(new RepositoryResponse(Collections.emptyList()));
    }
}
