package org.git.reporankerservice.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RestExceptionHandlerTest {

    private RestExceptionHandler restExceptionHandler;

    @BeforeEach
    void setUp() {
        restExceptionHandler = new RestExceptionHandler();
    }

    @Test
    void handleWebClientResponseException_shouldReturnErrorResponse() {
        // Given
        WebClientResponseException ex = new WebClientResponseException(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                null,
                "Internal Server Error".getBytes(StandardCharsets.UTF_8),
                StandardCharsets.UTF_8
        );

        // When
        ResponseEntity<String> response = restExceptionHandler.handleWebClientResponseException(ex);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Internal Server Error", response.getBody());
    }

    @Test
    void handleValidationExceptions_shouldReturnBadRequest() {
        // Given
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(
                new FieldError("objectName", "field", "defaultMessage")
        ));
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        // When
        ResponseEntity<String> response = restExceptionHandler.handleValidationExceptions(ex);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("field: defaultMessage", response.getBody());
    }

    @Test
    void handleGenericException_shouldReturnInternalServerError() {
        // Given
        Exception ex = new Exception("Generic error");

        // When
        ResponseEntity<String> response = restExceptionHandler.handleGenericException(ex);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An unexpected error occurred. Please try again later.", response.getBody());
    }
}
