package org.git.reporankerservice.client;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WebClientGitHubClientTest {

    private static MockWebServer mockWebServer;
    private WebClientGitHubClient gitHubClient;

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @BeforeEach
    void initialize() {
        String baseUrl = String.format("http://localhost:%s", mockWebServer.getPort());
        WebClient webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
        gitHubClient = new WebClientGitHubClient(webClient);
    }

    @AfterEach
    void reset() throws IOException {
        mockWebServer.shutdown();
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @Test
    void searchRepositories_shouldReturnRepositories_whenSuccessful() {
        // Given
        String jsonResponse = """
                {
                    "total_count": 1,
                    "incomplete_results": false,
                    "items": [
                        {
                            "id": 123,
                            "name": "test-repo",
                            "full_name": "test/test-repo",
                            "description": "A test repository",
                            "stargazers_count": 100,
                            "forks_count": 20,
                            "language": "Java",
                            "html_url": "https://github.com/test/test-repo"
                        }
                    ]
                }
                """;
        mockWebServer.enqueue(new MockResponse()
                .setBody(jsonResponse)
                .addHeader("Content-Type", "application/json"));

        LocalDate createdFrom = LocalDate.of(2023, 1, 1);
        String language = "Java";
        int page = 1;
        int size = 10;

        // When
        StepVerifier.create(gitHubClient.searchRepositories(createdFrom, language, page, size))
                // Then
                .assertNext(response -> {
                    assertNotNull(response);
                    assertFalse(response.items().isEmpty());
                    assertEquals(1, response.items().size());
                    assertEquals("test-repo", response.items().get(0).name());
                })
                .verifyComplete();
    }

    @Test
    void searchRepositories_shouldReturnEmpty_whenInvalidLanguage() {
        // Given
        LocalDate createdFrom = LocalDate.of(2023, 1, 1);
        String language = "InvalidLanguage";
        int page = 1;
        int size = 10;

        // When
        StepVerifier.create(gitHubClient.searchRepositories(createdFrom, language, page, size))
                // Then
                .assertNext(response -> {
                    assertNotNull(response);
                    assertTrue(response.items().isEmpty());
                })
                .verifyComplete();
    }

    @Test
    void searchRepositories_shouldReturnEmpty_whenApiReturnsError() {
        // Given
        mockWebServer.enqueue(new MockResponse().setResponseCode(500));

        LocalDate createdFrom = LocalDate.of(2023, 1, 1);
        String language = "Java";
        int page = 1;
        int size = 10;

        // When
        StepVerifier.create(gitHubClient.searchRepositories(createdFrom, language, page, size))
                // Then
                .assertNext(response -> {
                    assertNotNull(response);
                    assertTrue(response.items().isEmpty());
                })
                .verifyComplete();
    }
    
    @Test
    void searchRepositories_shouldBuildCorrectUri() throws InterruptedException {
        // Given
        mockWebServer.enqueue(new MockResponse()
                .setBody("{\"items\":[]}")
                .addHeader("Content-Type", "application/json"));

        LocalDate createdFrom = LocalDate.of(2023, 10, 20);
        String language = "Go";
        int page = 2;
        int size = 5;

        // When
        gitHubClient.searchRepositories(createdFrom, language, page, size).block();

        // Then
        var recordedRequest = mockWebServer.takeRequest();
        assertEquals("/search/repositories?q=created:%3E2023-10-20%20language:Go&sort=stars&order=desc&page=2&per_page=5", recordedRequest.getPath());
        assertEquals("GET", recordedRequest.getMethod());
    }
}
