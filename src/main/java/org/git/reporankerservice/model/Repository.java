package org.git.reporankerservice.model;

import java.time.OffsetDateTime;

public record Repository(
        String name,
        String owner,
        int stars,
        int forks,
        OffsetDateTime updatedAt,
        long score
) {}