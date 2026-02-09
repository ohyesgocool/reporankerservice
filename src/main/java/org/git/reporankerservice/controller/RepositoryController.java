package org.git.reporankerservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.git.reporankerservice.model.RepositoryRequest;
import org.git.reporankerservice.model.RepositoryResponse;
import org.git.reporankerservice.service.RepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/repositories")
@Tag(name = "Repository Search")
public class RepositoryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryController.class);
    private final RepositoryService repositoryService;

    public RepositoryController(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    @GetMapping("/search")
    @Operation(summary = "Get popular repositories")
    public CompletableFuture<RepositoryResponse> getRepositories(@ParameterObject @Valid RepositoryRequest request) {
        LOGGER.info("Received request to search repositories: {}", request);
        return repositoryService.getRepositories(request)
                .whenComplete((response, ex) -> {
                    if (ex != null) {
                        LOGGER.error("Error completing request for {}", request, ex);
                    } else {
                        LOGGER.info("Successfully completed request for {}", request);
                    }
                })
                .exceptionally(ex -> {
                    LOGGER.error("Unhandled exception for request {}", request, ex);
                    return new RepositoryResponse(Collections.emptyList());
                });
    }
}