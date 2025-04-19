package exception;

import com.tracktainment.duxmanager.exception.BusinessException;
import com.tracktainment.duxmanager.exception.ExceptionCode;
import com.tracktainment.duxmanager.exception.ParameterValidationErrorException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ParameterValidationFailedExceptionTest {

    @Test
    void shouldCreateParameterValidationFailedExceptionWithMessage() {
        // Arrange
        String message = "Parameter validation failed";

        // Act
        ParameterValidationErrorException exception = new ParameterValidationErrorException(message);

        // Assert
        assertEquals(ExceptionCode.PARAMETER_VALIDATION_ERROR.getCode(), exception.getCode());
        assertEquals(ExceptionCode.PARAMETER_VALIDATION_ERROR.getHttpStatusCode(), exception.getHttpStatusCode());
        assertEquals(ExceptionCode.PARAMETER_VALIDATION_ERROR.getReason(), exception.getReason());
        assertEquals(message, exception.getMessage());
    }

    @Test
    void shouldBeSubclassOfBusinessException() {
        // Arrange
        ParameterValidationErrorException exception = new ParameterValidationErrorException("Test message");

        // Assert
        assertTrue(exception instanceof BusinessException);
    }

    @Test
    void shouldHaveCorrectExceptionCode() {
        // Arrange
        ParameterValidationErrorException exception = new ParameterValidationErrorException("Test message");

        // Assert
        assertEquals(ExceptionCode.PARAMETER_VALIDATION_ERROR.getCode(), exception.getCode());
        assertEquals(400, exception.getHttpStatusCode());
        assertEquals("Parameter validation error.", exception.getReason());
    }
}
