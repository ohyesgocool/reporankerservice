package org.git.reporankerservice.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.git.reporankerservice.model.Language;

public class LanguageValidator implements ConstraintValidator<ValidLanguage, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return false;
        }
        return Language.isValid(value);
    }
}