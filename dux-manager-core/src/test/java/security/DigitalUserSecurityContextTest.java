package security;

import com.tracktainment.duxmanager.security.DigitalUserSecurityContext;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DigitalUserSecurityContextTest {

    @Test
    void shouldCreateContextUsingBuilder() {
        // Arrange
        String id = UUID.randomUUID().toString();
        String subject = "auth0|123456";

        // Act
        DigitalUserSecurityContext context = DigitalUserSecurityContext.builder()
                .id(id)
                .subject(subject)
                .build();

        // Assert
        assertEquals(id, context.getId());
        assertEquals(subject, context.getSubject());
    }

    @Test
    void shouldCreateEmptyContextUsingNoArgsConstructor() {
        // Act
        DigitalUserSecurityContext context = new DigitalUserSecurityContext();

        // Assert
        assertNull(context.getId());
        assertNull(context.getSubject());
    }

    @Test
    void shouldCreateContextUsingAllArgsConstructor() {
        // Arrange
        String id = UUID.randomUUID().toString();
        String subject = "auth0|123456";

        // Act
        DigitalUserSecurityContext context = new DigitalUserSecurityContext(id, subject);

        // Assert
        assertEquals(id, context.getId());
        assertEquals(subject, context.getSubject());
    }

    @Test
    void shouldUseSettersAndGetters() {
        // Arrange
        DigitalUserSecurityContext context = new DigitalUserSecurityContext();
        String id = UUID.randomUUID().toString();
        String subject = "auth0|123456";

        // Act
        context.setId(id);
        context.setSubject(subject);

        // Assert
        assertEquals(id, context.getId());
        assertEquals(subject, context.getSubject());
    }

    @Test
    void shouldImplementEqualsAndHashCode() {
        // Arrange
        String id1 = UUID.randomUUID().toString();
        String id2 = UUID.randomUUID().toString();
        String subject = "auth0|123456";

        DigitalUserSecurityContext context1 = DigitalUserSecurityContext.builder()
                .id(id1)
                .subject(subject)
                .build();

        DigitalUserSecurityContext context2 = DigitalUserSecurityContext.builder()
                .id(id1)  // Same ID
                .subject(subject)
                .build();

        DigitalUserSecurityContext context3 = DigitalUserSecurityContext.builder()
                .id(id2)  // Different ID
                .subject(subject)
                .build();

        // Assert
        assertEquals(context1, context2);
        assertNotEquals(context1, context3);
        assertEquals(context1.hashCode(), context2.hashCode());
        assertNotEquals(context1.hashCode(), context3.hashCode());
    }

    @Test
    void shouldImplementToString() {
        // Arrange
        DigitalUserSecurityContext context = DigitalUserSecurityContext.builder()
                .id(UUID.randomUUID().toString())
                .subject("auth0|123456")
                .build();

        // Act
        String toString = context.toString();

        // Assert
        assertNotNull(toString);
        assertFalse(toString.isEmpty());
        assertTrue(toString.contains("DigitalUserSecurityContext"));
    }
}
