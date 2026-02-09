package org.git.reporankerservice.model.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record GitHubSearchResponse(
        @JsonProperty("total_count") int totalCount,
        @JsonProperty("incomplete_results") boolean incompleteResults,
        @JsonProperty("items") List<GitHubRepository> items
) {
    public GitHubSearchResponse(List<GitHubRepository> items) {
        this(0, false, items);
    }
}
