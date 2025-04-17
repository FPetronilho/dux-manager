package exception;

import com.tracktainment.duxmanager.exception.ExceptionCode;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionCodeTest {

    @Test
    void shouldHaveCorrectValuesForInternalServerError() {
        // Act & Assert
        assertEquals("E-001", ExceptionCode.INTERNAL_SERVER_ERROR.getCode());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), ExceptionCode.INTERNAL_SERVER_ERROR.getHttpStatusCode());
        assertEquals("Internal server error.", ExceptionCode.INTERNAL_SERVER_ERROR.getReason());
    }

    @Test
    void shouldHaveCorrectValuesForResourceNotFound() {
        // Act & Assert
        assertEquals("E-002", ExceptionCode.RESOURCE_NOT_FOUND.getCode());
        assertEquals(HttpStatus.NOT_FOUND.value(), ExceptionCode.RESOURCE_NOT_FOUND.getHttpStatusCode());
        assertEquals("Resource not found.", ExceptionCode.RESOURCE_NOT_FOUND.getReason());
    }

    @Test
    void shouldHaveCorrectValuesForResourceAlreadyExists() {
        // Act & Assert
        assertEquals("E-003", ExceptionCode.RESOURCE_ALREADY_EXISTS.getCode());
        assertEquals(HttpStatus.CONFLICT.value(), ExceptionCode.RESOURCE_ALREADY_EXISTS.getHttpStatusCode());
        assertEquals("Resource already exists.", ExceptionCode.RESOURCE_ALREADY_EXISTS.getReason());
    }

    @Test
    void shouldHaveCorrectValuesForClientNotAuthenticated() {
        // Act & Assert
        assertEquals("E-004", ExceptionCode.CLIENT_NOT_AUTHENTICATED.getCode());
        assertEquals(HttpStatus.UNAUTHORIZED.value(), ExceptionCode.CLIENT_NOT_AUTHENTICATED.getHttpStatusCode());
        assertEquals("Client not authenticated.", ExceptionCode.CLIENT_NOT_AUTHENTICATED.getReason());
    }

    @Test
    void shouldHaveCorrectValuesForClientNotAuthorized() {
        // Act & Assert
        assertEquals("E-005", ExceptionCode.CLIENT_NOT_AUTHORIZED.getCode());
        assertEquals(HttpStatus.FORBIDDEN.value(), ExceptionCode.CLIENT_NOT_AUTHORIZED.getHttpStatusCode());
        assertEquals("Client not authorized.", ExceptionCode.CLIENT_NOT_AUTHORIZED.getReason());
    }

    @Test
    void shouldHaveCorrectValuesForConfigurationError() {
        // Act & Assert
        assertEquals("E-006", ExceptionCode.CONFIGURATION_ERROR.getCode());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), ExceptionCode.CONFIGURATION_ERROR.getHttpStatusCode());
        assertEquals("Configuration error.", ExceptionCode.CONFIGURATION_ERROR.getReason());
    }

    @Test
    void shouldHaveCorrectValuesForParameterValidationError() {
        // Act & Assert
        assertEquals("E-007", ExceptionCode.PARAMETER_VALIDATION_ERROR.getCode());
        assertEquals(HttpStatus.BAD_REQUEST.value(), ExceptionCode.PARAMETER_VALIDATION_ERROR.getHttpStatusCode());
        assertEquals("Parameter validation error.", ExceptionCode.PARAMETER_VALIDATION_ERROR.getReason());
    }

    @Test
    void shouldHaveCorrectToStringRepresentation() {
        // Act & Assert
        assertTrue(ExceptionCode.INTERNAL_SERVER_ERROR.toString().contains("INTERNAL_SERVER_ERROR"));
        assertTrue(ExceptionCode.RESOURCE_NOT_FOUND.toString().contains("RESOURCE_NOT_FOUND"));
        assertTrue(ExceptionCode.RESOURCE_ALREADY_EXISTS.toString().contains("RESOURCE_ALREADY_EXISTS"));
        assertTrue(ExceptionCode.CLIENT_NOT_AUTHENTICATED.toString().contains("CLIENT_NOT_AUTHENTICATED"));
        assertTrue(ExceptionCode.CLIENT_NOT_AUTHORIZED.toString().contains("CLIENT_NOT_AUTHORIZED"));
        assertTrue(ExceptionCode.CONFIGURATION_ERROR.toString().contains("CONFIGURATION_ERROR"));
        assertTrue(ExceptionCode.PARAMETER_VALIDATION_ERROR.toString().contains("PARAMETER_VALIDATION_ERROR"));
    }

    @Test
    void shouldHaveSevenExceptionCodes() {
        // Act & Assert
        assertEquals(7, ExceptionCode.values().length);
    }
}
