package encryption;

import com.tracktainment.duxmanager.encryption.EncryptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EncryptionServiceTest {

    @InjectMocks
    private EncryptionService encryptionService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(encryptionService, "secretKey", "test-secret-key-that-is-at-least-32-chars");
        ReflectionTestUtils.setField(encryptionService, "salt", "test-salt-value");
    }

    @Test
    void shouldEncryptAndDecryptSuccessfully() {
        // Arrange
        String plainText = "sensitive data";

        // Act
        String encrypted = encryptionService.encrypt(plainText);
        String decrypted = encryptionService.decrypt(encrypted);

        // Assert
        assertNotNull(encrypted);
        assertNotEquals(plainText, encrypted);
        assertEquals(plainText, decrypted);
    }

    @Test
    void shouldReturnNullWhenEncryptingNull() {
        // Arrange & Act
        String result = encryptionService.encrypt(null);

        // Assert
        assertNull(result);
    }

    @Test
    void shouldReturnNullWhenDecryptingNull() {
        // Arrange & Act
        String result = encryptionService.decrypt(null);

        // Assert
        assertNull(result);
    }

    @Test
    void shouldHandleEmptyStringEncryption() {
        // Arrange
        String emptyString = "";

        // Act
        String encrypted = encryptionService.encrypt(emptyString);
        String decrypted = encryptionService.decrypt(encrypted);

        // Assert
        assertNotNull(encrypted);
        assertNotEquals(emptyString, encrypted);
        assertEquals(emptyString, decrypted);
    }
}
