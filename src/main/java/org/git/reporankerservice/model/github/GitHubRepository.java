package org.git.reporankerservice.model.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record GitHubRepository(
        String name,
        GitHubOwner owner,
        @JsonProperty("stargazers_count") int stars,
        @JsonProperty("forks_count") int forks,
        @JsonProperty("pushed_at") OffsetDateTime pushedAt
) {}
