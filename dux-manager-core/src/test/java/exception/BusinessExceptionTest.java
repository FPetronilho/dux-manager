package exception;

import com.tracktainment.duxmanager.exception.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

class BusinessExceptionTest {

    @Test
    void shouldCreateBusinessExceptionWithExceptionCodeAndMessage() {
        // Arrange
        String errorMessage = "Test error message";
        ExceptionCode exceptionCode = ExceptionCode.CLIENT_NOT_AUTHENTICATED;

        // Act
        BusinessException exception = new BusinessException(exceptionCode, errorMessage);

        // Assert
        assertEquals(exceptionCode.getCode(), exception.getCode());
        assertEquals(exceptionCode.getHttpStatusCode(), exception.getHttpStatusCode());
        assertEquals(exceptionCode.getReason(), exception.getReason());
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void shouldCreateBusinessExceptionWithExceptionCodeOnly() {
        // Arrange
        ExceptionCode exceptionCode = ExceptionCode.INTERNAL_SERVER_ERROR;

        // Act
        BusinessException exception = new BusinessException(exceptionCode);

        // Assert
        assertEquals(exceptionCode.getCode(), exception.getCode());
        assertEquals(exceptionCode.getHttpStatusCode(), exception.getHttpStatusCode());
        assertEquals(exceptionCode.getReason(), exception.getReason());
        assertNull(exception.getMessage());
    }

    @Test
    void shouldInheritFromRuntimeException() {
        // Arrange
        BusinessException exception = new BusinessException(ExceptionCode.CLIENT_NOT_AUTHENTICATED);

        // Assert
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void shouldCreateResourceNotFoundException() {
        // Arrange
        Class<?> resourceClass = DigitalUser.class;
        String resourceId = "123";

        // Act
        ResourceNotFoundException exception = new ResourceNotFoundException(resourceClass, resourceId);

        // Assert
        assertEquals(ExceptionCode.RESOURCE_NOT_FOUND.getCode(), exception.getCode());
        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getHttpStatusCode());
        assertEquals(String.format(ResourceNotFoundException.ERROR_MESSAGE, resourceClass.getSimpleName(), resourceId),
                exception.getMessage());
    }

    @Test
    void shouldCreateResourceAlreadyExistsException() {
        // Arrange
        Class<?> resourceClass = Asset.class;
        String resourceId = "456";

        // Act
        ResourceAlreadyExistsException exception = new ResourceAlreadyExistsException(resourceClass, resourceId);

        // Assert
        assertEquals(ExceptionCode.RESOURCE_ALREADY_EXISTS.getCode(), exception.getCode());
        assertEquals(HttpStatus.CONFLICT.value(), exception.getHttpStatusCode());
        assertEquals(String.format(ResourceAlreadyExistsException.ERROR_MESSAGE, resourceClass.getSimpleName(), resourceId),
                exception.getMessage());
    }

    @Test
    void shouldCreateAuthenticationFailedException() {
        // Arrange
        String errorMessage = "User not authenticated";

        // Act
        AuthenticationFailedException exception = new AuthenticationFailedException(errorMessage);

        // Assert
        assertEquals(ExceptionCode.CLIENT_NOT_AUTHENTICATED.getCode(), exception.getCode());
        assertEquals(HttpStatus.UNAUTHORIZED.value(), exception.getHttpStatusCode());
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void shouldCreateAuthorizationFailedException() {
        // Arrange
        String errorMessage = "User not authorized to access this resource";

        // Act
        AuthorizationFailedException exception = new AuthorizationFailedException(errorMessage);

        // Assert
        assertEquals(ExceptionCode.CLIENT_NOT_AUTHORIZED.getCode(), exception.getCode());
        assertEquals(HttpStatus.FORBIDDEN.value(), exception.getHttpStatusCode());
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void shouldCreateInternalServerErrorException() {
        // Arrange
        String errorMessage = "Internal server error occurred";

        // Act
        InternalServerErrorException exception = new InternalServerErrorException(errorMessage);

        // Assert
        assertEquals(ExceptionCode.INTERNAL_SERVER_ERROR.getCode(), exception.getCode());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getHttpStatusCode());
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void shouldCreateParameterValidationErrorException() {
        // Arrange
        String errorMessage = "Parameter validation failed";

        // Act
        ParameterValidationErrorException exception = new ParameterValidationErrorException(errorMessage);

        // Assert
        assertEquals(ExceptionCode.PARAMETER_VALIDATION_ERROR.getCode(), exception.getCode());
        assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getHttpStatusCode());
        assertEquals(errorMessage, exception.getMessage());
    }

    // Helper classes to avoid dependency on domain classes
    private static class DigitalUser {}
    private static class Asset {}
}
