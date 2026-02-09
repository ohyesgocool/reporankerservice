package org.git.reporankerservice.model.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record GitHubSearchResponse(
        @JsonProperty("items") List<GitHubRepository> items
) {}
