package exception;

import com.tracktainment.duxmanager.exception.BusinessException;
import com.tracktainment.duxmanager.exception.ExceptionCode;
import com.tracktainment.duxmanager.exception.InternalServerErrorException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class InternalServerErrorExceptionTest {

    @Test
    void shouldCreateInternalServerErrorExceptionWithMessage() {
        // Arrange
        String message = "An internal server error occurred";

        // Act
        InternalServerErrorException exception = new InternalServerErrorException(message);

        // Assert
        assertEquals(ExceptionCode.INTERNAL_SERVER_ERROR.getCode(), exception.getCode());
        assertEquals(ExceptionCode.INTERNAL_SERVER_ERROR.getHttpStatusCode(), exception.getHttpStatusCode());
        assertEquals(ExceptionCode.INTERNAL_SERVER_ERROR.getReason(), exception.getReason());
        assertEquals(message, exception.getMessage());
    }

    @Test
    void shouldBeSubclassOfBusinessException() {
        // Arrange
        InternalServerErrorException exception = new InternalServerErrorException("Test message");

        // Assert
        assertTrue(exception instanceof BusinessException);
    }

    @Test
    void shouldHaveCorrectExceptionCode() {
        // Arrange
        InternalServerErrorException exception = new InternalServerErrorException("Test message");

        // Assert
        assertEquals(ExceptionCode.INTERNAL_SERVER_ERROR.getCode(), exception.getCode());
        assertEquals(500, exception.getHttpStatusCode());
        assertEquals("Internal server error.", exception.getReason());
    }
}
