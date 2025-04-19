package exception;

import com.tracktainment.duxmanager.exception.AuthorizationFailedException;
import com.tracktainment.duxmanager.exception.BusinessException;
import com.tracktainment.duxmanager.exception.ExceptionCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthorizationFailedExceptionTest {

    @Test
    void shouldCreateAuthorizationFailedExceptionWithMessage() {
        // Arrange
        String message = "Authorization failed";

        // Act
        AuthorizationFailedException exception = new AuthorizationFailedException(message);

        // Assert
        assertEquals(ExceptionCode.CLIENT_NOT_AUTHORIZED.getCode(), exception.getCode());
        assertEquals(ExceptionCode.CLIENT_NOT_AUTHORIZED.getHttpStatusCode(), exception.getHttpStatusCode());
        assertEquals(ExceptionCode.CLIENT_NOT_AUTHORIZED.getReason(), exception.getReason());
        assertEquals(message, exception.getMessage());
    }

    @Test
    void shouldBeSubclassOfBusinessException() {
        // Arrange
        AuthorizationFailedException exception = new AuthorizationFailedException("Test message");

        // Assert
        assertTrue(exception instanceof BusinessException);
    }

    @Test
    void shouldHaveCorrectExceptionCode() {
        // Arrange
        AuthorizationFailedException exception = new AuthorizationFailedException("Test message");

        // Assert
        assertEquals(ExceptionCode.CLIENT_NOT_AUTHORIZED.getCode(), exception.getCode());
        assertEquals(403, exception.getHttpStatusCode());
        assertEquals("Client not authorized.", exception.getReason());
    }
}
