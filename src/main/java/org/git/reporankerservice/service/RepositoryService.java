package org.git.reporankerservice.service;

import org.git.reporankerservice.model.RepositoryRequest;
import org.git.reporankerservice.model.RepositoryResponse;

import java.util.concurrent.CompletableFuture;

public interface RepositoryService {
    CompletableFuture<RepositoryResponse> getRepositories(RepositoryRequest request);
}