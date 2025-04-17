package document;

import com.tracktainment.duxmanager.document.BaseDocument;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BaseDocumentTest {

    @Test
    void shouldCreateBaseDocumentUsingBuilder() {
        // Arrange
        ObjectId dbId = new ObjectId();
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now().plusDays(1);

        // Act
        BaseDocument baseDocument = BaseDocument.builder()
                .dbId(dbId)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        // Assert
        assertEquals(dbId, baseDocument.getDbId());
        assertEquals(createdAt, baseDocument.getCreatedAt());
        assertEquals(updatedAt, baseDocument.getUpdatedAt());
    }

    @Test
    void shouldCreateEmptyBaseDocumentUsingNoArgsConstructor() {
        // Act
        BaseDocument baseDocument = new BaseDocument();

        // Assert
        assertNull(baseDocument.getDbId());
        assertNull(baseDocument.getCreatedAt());
        assertNull(baseDocument.getUpdatedAt());
    }

    @Test
    void shouldUseSettersAndGetters() {
        // Arrange
        BaseDocument baseDocument = new BaseDocument();
        ObjectId dbId = new ObjectId();
        LocalDateTime now = LocalDateTime.now();

        // Act
        baseDocument.setDbId(dbId);
        baseDocument.setCreatedAt(now);
        baseDocument.setUpdatedAt(now);

        // Assert
        assertEquals(dbId, baseDocument.getDbId());
        assertEquals(now, baseDocument.getCreatedAt());
        assertEquals(now, baseDocument.getUpdatedAt());
    }

    @Test
    void shouldTestIsNewMethod() {
        // Arrange
        BaseDocument newDocument = new BaseDocument();

        BaseDocument existingDocument = new BaseDocument();
        existingDocument.setUpdatedAt(LocalDateTime.now());

        // Act & Assert - Using reflection to access private method
        try {
            java.lang.reflect.Method isNewMethod = BaseDocument.class.getDeclaredMethod("isNew");
            isNewMethod.setAccessible(true);

            assertTrue((Boolean) isNewMethod.invoke(newDocument), "Document with null updatedAt should be considered new");
            assertFalse((Boolean) isNewMethod.invoke(existingDocument), "Document with non-null updatedAt should not be considered new");
        } catch (Exception e) {
            fail("Failed to test isNew method: " + e.getMessage());
        }
    }

    @Test
    void shouldCreateBaseDocumentWithAllArgsConstructor() {
        // Arrange
        ObjectId dbId = new ObjectId();
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now().plusDays(1);

        // Act
        BaseDocument baseDocument = new BaseDocument(dbId, createdAt, updatedAt);

        // Assert
        assertEquals(dbId, baseDocument.getDbId());
        assertEquals(createdAt, baseDocument.getCreatedAt());
        assertEquals(updatedAt, baseDocument.getUpdatedAt());
    }

    @Test
    void shouldImplementEqualsAndHashCode() {
        // Arrange
        ObjectId dbId1 = new ObjectId();
        ObjectId dbId2 = new ObjectId();

        BaseDocument doc1 = BaseDocument.builder().dbId(dbId1).build();
        BaseDocument doc2 = BaseDocument.builder().dbId(dbId1).build(); // Same dbId
        BaseDocument doc3 = BaseDocument.builder().dbId(dbId2).build(); // Different dbId

        // Assert
        assertEquals(doc1, doc1); // Same object reference
        assertEquals(doc1, doc2); // Equal by dbId
        assertNotEquals(doc1, doc3); // Different dbId
        assertEquals(doc1.hashCode(), doc2.hashCode()); // Same hash code for equal objects
        assertNotEquals(doc1.hashCode(), doc3.hashCode()); // Different hash code for different objects
        assertNotEquals(doc1, null); // Not equal to null
        assertNotEquals(doc1, "not a BaseDocument"); // Not equal to different type
    }

    @Test
    void shouldImplementToString() {
        // Arrange
        ObjectId dbId = new ObjectId();
        BaseDocument baseDocument = BaseDocument.builder()
                .dbId(dbId)
                .build();

        // Act
        String toString = baseDocument.toString();

        // Assert
        assertNotNull(toString);
        assertFalse(toString.isEmpty());
        assertTrue(toString.contains("BaseDocument"));
        assertTrue(toString.contains(dbId.toString()));
    }
}
