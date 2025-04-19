package exception;

import com.tracktainment.duxmanager.domain.Asset;
import com.tracktainment.duxmanager.exception.BusinessException;
import com.tracktainment.duxmanager.exception.ExceptionCode;
import com.tracktainment.duxmanager.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ResourceNotFoundExceptionTest {

    @Test
    void shouldCreateResourceNotFoundExceptionWithClassAndAttribute() {
        // Arrange
        Class<?> resourceClass = Asset.class;
        String resourceAttribute = UUID.randomUUID().toString();

        // Act
        ResourceNotFoundException exception = new ResourceNotFoundException(resourceClass, resourceAttribute);

        // Assert
        assertEquals(ExceptionCode.RESOURCE_NOT_FOUND.getCode(), exception.getCode());
        assertEquals(ExceptionCode.RESOURCE_NOT_FOUND.getHttpStatusCode(), exception.getHttpStatusCode());
        assertEquals(ExceptionCode.RESOURCE_NOT_FOUND.getReason(), exception.getReason());
        assertEquals(String.format("Asset %s not found.", resourceAttribute), exception.getMessage());
    }

    @Test
    void shouldBeSubclassOfBusinessException() {
        // Arrange
        ResourceNotFoundException exception = new ResourceNotFoundException(Asset.class, "test");

        // Assert
        assertTrue(exception instanceof BusinessException);
    }

    @Test
    void shouldHaveCorrectExceptionCode() {
        // Arrange
        ResourceNotFoundException exception = new ResourceNotFoundException(Asset.class, "test");

        // Assert
        assertEquals(ExceptionCode.RESOURCE_NOT_FOUND.getCode(), exception.getCode());
        assertEquals(404, exception.getHttpStatusCode());
        assertEquals("Resource not found.", exception.getReason());
    }

    @Test
    void shouldFormatMessageWithDifferentResourceTypes() {
        // Arrange
        String assetId = UUID.randomUUID().toString();

        // Act
        ResourceNotFoundException gameException = new ResourceNotFoundException(Asset.class, assetId);
        ResourceNotFoundException integerException = new ResourceNotFoundException(Integer.class, "42");

        // Assert
        assertEquals(String.format("Asset %s not found.", assetId), gameException.getMessage());
        assertEquals("Integer 42 not found.", integerException.getMessage());
    }
}