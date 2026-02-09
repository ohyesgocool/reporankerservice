package org.git.reporankerservice.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = LanguageValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidLanguage {
    String message() default "Invalid language";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}