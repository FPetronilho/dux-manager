package annotation;

import com.tracktainment.duxmanager.annotation.Encrypted;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class EncryptedAnnotationTest {

    @Test
    void shouldAnnotateFieldWithEncrypted() throws NoSuchFieldException {
        // Arrange
        Class<TestClass> testClass = TestClass.class;

        // Act
        Field sensitiveField = testClass.getDeclaredField("sensitiveData");
        Field normalField = testClass.getDeclaredField("normalData");

        // Assert
        assertTrue(sensitiveField.isAnnotationPresent(Encrypted.class));
        assertFalse(normalField.isAnnotationPresent(Encrypted.class));
    }

    private static class TestClass {
        @Encrypted
        private String sensitiveData;

        private String normalData;
    }
}
