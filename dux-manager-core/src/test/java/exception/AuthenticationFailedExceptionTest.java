package exception;
import com.tracktainment.duxmanager.exception.AuthenticationFailedException;
import com.tracktainment.duxmanager.exception.BusinessException;
import com.tracktainment.duxmanager.exception.ExceptionCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationFailedExceptionTest {

    @Test
    void shouldCreateAuthenticationFailedExceptionWithMessage() {
        // Arrange
        String message = "Authentication failed";

        // Act
        AuthenticationFailedException exception = new AuthenticationFailedException(message);

        // Assert
        assertEquals(ExceptionCode.CLIENT_NOT_AUTHENTICATED.getCode(), exception.getCode());
        assertEquals(ExceptionCode.CLIENT_NOT_AUTHENTICATED.getHttpStatusCode(), exception.getHttpStatusCode());
        assertEquals(ExceptionCode.CLIENT_NOT_AUTHENTICATED.getReason(), exception.getReason());
        assertEquals(message, exception.getMessage());
    }

    @Test
    void shouldBeSubclassOfBusinessException() {
        // Arrange
        AuthenticationFailedException exception = new AuthenticationFailedException("Test message");

        // Assert
        assertTrue(exception instanceof BusinessException);
    }

    @Test
    void shouldHaveCorrectExceptionCode() {
        // Arrange
        AuthenticationFailedException exception = new AuthenticationFailedException("Test message");

        // Assert
        assertEquals(ExceptionCode.CLIENT_NOT_AUTHENTICATED.getCode(), exception.getCode());
        assertEquals(401, exception.getHttpStatusCode());
        assertEquals("Client not authenticated.", exception.getReason());
    }
}
