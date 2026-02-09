package org.git.reporankerservice.model;

import java.util.Arrays;

public enum Language {
    JAVA, JAVASCRIPT, PYTHON, RUBY, PHP, C, CPLUSPLUS, CSHARP, GO, SWIFT, KOTLIN, RUST, TYPESCRIPT;

    public static boolean isValid(String lang) {
        return Arrays.stream(Language.values())
                .map(Language::name)
                .anyMatch(name -> name.equalsIgnoreCase(lang));
    }
}
