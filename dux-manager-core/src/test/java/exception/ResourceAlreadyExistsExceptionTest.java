package exception;

import com.tracktainment.duxmanager.domain.Asset;
import com.tracktainment.duxmanager.exception.BusinessException;
import com.tracktainment.duxmanager.exception.ExceptionCode;
import com.tracktainment.duxmanager.exception.ResourceAlreadyExistsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ResourceAlreadyExistsExceptionTest {

    @Test
    void shouldCreateResourceAlreadyExistsExceptionWithClassAndAttribute() {
        // Arrange
        Class<?> resourceClass = Asset.class;
        String resourceAttribute = "The Last of Us Part II";

        // Act
        ResourceAlreadyExistsException exception = new ResourceAlreadyExistsException(resourceClass, resourceAttribute);

        // Assert
        assertEquals(ExceptionCode.RESOURCE_ALREADY_EXISTS.getCode(), exception.getCode());
        assertEquals(ExceptionCode.RESOURCE_ALREADY_EXISTS.getHttpStatusCode(), exception.getHttpStatusCode());
        assertEquals(ExceptionCode.RESOURCE_ALREADY_EXISTS.getReason(), exception.getReason());
        assertEquals(String.format("Asset %s already exists.", resourceAttribute), exception.getMessage());
    }

    @Test
    void shouldBeSubclassOfBusinessException() {
        // Arrange
        ResourceAlreadyExistsException exception = new ResourceAlreadyExistsException(Asset.class, "test");

        // Assert
        assertTrue(exception instanceof BusinessException);
    }

    @Test
    void shouldHaveCorrectExceptionCode() {
        // Arrange
        ResourceAlreadyExistsException exception = new ResourceAlreadyExistsException(Asset.class, "test");

        // Assert
        assertEquals(ExceptionCode.RESOURCE_ALREADY_EXISTS.getCode(), exception.getCode());
        assertEquals(409, exception.getHttpStatusCode());
        assertEquals("Resource already exists.", exception.getReason());
    }

    @Test
    void shouldFormatMessageWithDifferentResourceTypes() {
        // Arrange
        String assetId = UUID.randomUUID().toString();

        // Act
        ResourceAlreadyExistsException gameException = new ResourceAlreadyExistsException(Asset.class, assetId);
        ResourceAlreadyExistsException stringException = new ResourceAlreadyExistsException(String.class, "test");

        // Assert
        assertEquals(String.format("Asset %s already exists.", assetId), gameException.getMessage());
        assertEquals("String test already exists.", stringException.getMessage());
    }
}
