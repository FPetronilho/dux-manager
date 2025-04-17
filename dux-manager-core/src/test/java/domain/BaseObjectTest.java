package domain;

import com.tracktainment.duxmanager.domain.BaseObject;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BaseObjectTest {

    @Test
    void shouldCreateBaseObjectUsingBuilder() {
        // Arrange
        String id = UUID.randomUUID().toString();
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now().plusDays(1);

        // Act
        BaseObject baseObject = BaseObject.builder()
                .id(id)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        // Assert
        assertEquals(id, baseObject.getId());
        assertEquals(createdAt, baseObject.getCreatedAt());
        assertEquals(updatedAt, baseObject.getUpdatedAt());
    }

    @Test
    void shouldCreateEmptyBaseObjectUsingNoArgsConstructor() {
        // Act
        BaseObject baseObject = new BaseObject();

        // Assert
        assertNull(baseObject.getId());
        assertNull(baseObject.getCreatedAt());
        assertNull(baseObject.getUpdatedAt());
    }

    @Test
    void shouldUseSettersAndGetters() {
        // Arrange
        BaseObject baseObject = new BaseObject();
        String id = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();

        // Act
        baseObject.setId(id);
        baseObject.setCreatedAt(now);
        baseObject.setUpdatedAt(now);

        // Assert
        assertEquals(id, baseObject.getId());
        assertEquals(now, baseObject.getCreatedAt());
        assertEquals(now, baseObject.getUpdatedAt());
    }

    @Test
    void shouldImplementEqualsAndHashCodeCorrectly() {
        // Arrange
        String id1 = UUID.randomUUID().toString();
        String id2 = UUID.randomUUID().toString();

        BaseObject obj1 = BaseObject.builder().id(id1).build();
        BaseObject obj2 = BaseObject.builder().id(id1).build(); // Same ID
        BaseObject obj3 = BaseObject.builder().id(id2).build(); // Different ID

        // Assert
        assertEquals(obj1, obj2); // Same ID should be equal
        assertNotEquals(obj1, obj3); // Different ID should not be equal
        assertEquals(obj1.hashCode(), obj2.hashCode()); // Same ID should have same hashCode
        assertNotEquals(obj1.hashCode(), obj3.hashCode()); // Different ID should have different hashCode
    }

    @Test
    void shouldImplementToString() {
        // Arrange
        String id = UUID.randomUUID().toString();
        BaseObject baseObject = BaseObject.builder()
                .id(id)
                .build();

        // Act
        String toString = baseObject.toString();

        // Assert
        assertNotNull(toString);
        assertFalse(toString.isEmpty());
        assertTrue(toString.contains("BaseObject"));
    }

    @Test
    void shouldEqualsReturnFalseForNull() {
        // Arrange
        BaseObject baseObject = BaseObject.builder()
                .id(UUID.randomUUID().toString())
                .build();

        // Assert
        assertNotEquals(null, baseObject);
    }

    @Test
    void shouldEqualsReturnFalseForDifferentClass() {
        // Arrange
        BaseObject baseObject = BaseObject.builder()
                .id(UUID.randomUUID().toString())
                .build();

        String notABaseObject = "not a base object";

        // Assert
        assertNotEquals(notABaseObject, baseObject);
    }

    @Test
    void shouldEqualsReturnTrueForSameObject() {
        // Arrange
        BaseObject baseObject = BaseObject.builder()
                .id(UUID.randomUUID().toString())
                .build();

        // Assert
        assertEquals(baseObject, baseObject);
    }
}
