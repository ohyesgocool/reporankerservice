package org.git.reporankerservice.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LanguageValidatorTest {

    private LanguageValidator validator;

    @BeforeEach
    void setUp() {
        validator = new LanguageValidator();
    }

    @ParameterizedTest
    @ValueSource(strings = {"java", "python", "go", "javascript", "ruby", "c", "csharp", "cplusplus"})
    void isValid_shouldReturnTrue_forValidLanguages(String language) {
        assertTrue(validator.isValid(language, null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "  ", "invalid-language"})
    void isValid_shouldReturnFalse_forInvalidLanguages(String language) {
        assertFalse(validator.isValid(language, null));
    }

    @Test
    void isValid_shouldReturnFalse_forNull() {
        assertFalse(validator.isValid(null, null));
    }
}
