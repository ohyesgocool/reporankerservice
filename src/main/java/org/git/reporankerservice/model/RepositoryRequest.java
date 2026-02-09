package org.git.reporankerservice.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.git.reporankerservice.validation.ValidLanguage;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record RepositoryRequest(
        @NotNull(message = "Date cannot be null")
        @PastOrPresent(message = "Date cannot be in the future")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate createdFrom,

        @ValidLanguage
        String language,

        @Min(value = 1, message = "Page index must be at least 1")
        Integer page,

        @Min(value = 1, message = "Page size must be at least 1")
        Integer size
) {
    public RepositoryRequest {
        if (page == null) page = 1;  // GitHub starts at page 1
        if (size == null) size = 20;
    }
}