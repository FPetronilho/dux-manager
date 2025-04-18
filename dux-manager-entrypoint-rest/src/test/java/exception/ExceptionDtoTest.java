package exception;

import com.tracktainment.duxmanager.exception.ExceptionDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionDtoTest {

    @Test
    void shouldCreateExceptionDtoUsingBuilder() {
        // Arrange
        String code = "E-001";
        int httpStatusCode = 500;
        String reason = "Internal server error";
        String message = "An unexpected error occurred";

        // Act
        ExceptionDto exceptionDto = ExceptionDto.builder()
                .code(code)
                .httpStatusCode(httpStatusCode)
                .reason(reason)
                .message(message)
                .build();

        // Assert
        assertEquals(code, exceptionDto.getCode());
        assertEquals(httpStatusCode, exceptionDto.getHttpStatusCode());
        assertEquals(reason, exceptionDto.getReason());
        assertEquals(message, exceptionDto.getMessage());
    }

    @Test
    void shouldCreateEmptyExceptionDtoUsingNoArgsConstructor() {
        // Act
        ExceptionDto exceptionDto = new ExceptionDto();

        // Assert
        assertNull(exceptionDto.getCode());
        assertEquals(0, exceptionDto.getHttpStatusCode());
        assertNull(exceptionDto.getReason());
        assertNull(exceptionDto.getMessage());
    }

    @Test
    void shouldUseSettersAndGetters() {
        // Arrange
        ExceptionDto exceptionDto = new ExceptionDto();
        String code = "E-002";
        int httpStatusCode = 404;
        String reason = "Resource not found";
        String message = "The requested resource was not found";

        // Act
        exceptionDto.setCode(code);
        exceptionDto.setHttpStatusCode(httpStatusCode);
        exceptionDto.setReason(reason);
        exceptionDto.setMessage(message);

        // Assert
        assertEquals(code, exceptionDto.getCode());
        assertEquals(httpStatusCode, exceptionDto.getHttpStatusCode());
        assertEquals(reason, exceptionDto.getReason());
        assertEquals(message, exceptionDto.getMessage());
    }

    @Test
    void shouldCreateExceptionDtoUsingAllArgsConstructor() {
        // Arrange
        String code = "E-003";
        int httpStatusCode = 409;
        String reason = "Resource already exists";
        String message = "The resource already exists";

        // Act
        ExceptionDto exceptionDto = new ExceptionDto(code, httpStatusCode, reason, message);

        // Assert
        assertEquals(code, exceptionDto.getCode());
        assertEquals(httpStatusCode, exceptionDto.getHttpStatusCode());
        assertEquals(reason, exceptionDto.getReason());
        assertEquals(message, exceptionDto.getMessage());
    }

    @Test
    void shouldImplementEqualsAndHashCode() {
        // Arrange
        ExceptionDto dto1 = ExceptionDto.builder()
                .code("E-001")
                .httpStatusCode(500)
                .reason("Internal server error")
                .message("An error occurred")
                .build();

        ExceptionDto dto2 = ExceptionDto.builder()
                .code("E-001")
                .httpStatusCode(500)
                .reason("Internal server error")
                .message("An error occurred")
                .build();

        ExceptionDto dto3 = ExceptionDto.builder()
                .code("E-002")
                .httpStatusCode(404)
                .reason("Resource not found")
                .message("Not found")
                .build();

        // Assert
        assertEquals(dto1, dto1); // Same object reference
        assertEquals(dto1, dto2); // Equal by value
        assertNotEquals(dto1, dto3); // Different values
        assertEquals(dto1.hashCode(), dto2.hashCode()); // Same hash code for equal objects
        assertNotEquals(dto1.hashCode(), dto3.hashCode()); // Different hash code for different objects
        assertNotEquals(dto1, null); // Not equal to null
        assertNotEquals(dto1, "not an ExceptionDto"); // Not equal to different type
    }

    @Test
    void shouldImplementToString() {
        // Arrange
        ExceptionDto dto = ExceptionDto.builder()
                .code("E-001")
                .httpStatusCode(500)
                .reason("Internal server error")
                .message("An error occurred")
                .build();

        // Act
        String toString = dto.toString();

        // Assert
        assertNotNull(toString);
        assertFalse(toString.isEmpty());
        assertTrue(toString.contains("ExceptionDto"));
        assertTrue(toString.contains("E-001"));
        assertTrue(toString.contains("500"));
        assertTrue(toString.contains("Internal server error"));
        assertTrue(toString.contains("An error occurred"));
    }

    @Test
    void shouldHandleJsonIncludeAnnotation() {
        // Arrange
        ExceptionDto dto = ExceptionDto.builder()
                .code("E-001")
                .httpStatusCode(500)
                .reason("Internal server error")
                // No message provided
                .build();

        // Assert
        assertNotNull(dto);
        assertNull(dto.getMessage());

        /*
        The @JsonInclude(JsonInclude.Include.NON_NULL) annotation should
        ensure that null fields are not included in JSON serialization.
        This is hard to test directly in a unit test without serialization,
        but we can verify the annotation exists on the class.
        */
        com.fasterxml.jackson.annotation.JsonInclude jsonInclude =
                ExceptionDto.class.getAnnotation(com.fasterxml.jackson.annotation.JsonInclude.class);

        assertNotNull(jsonInclude);
        assertEquals(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL, jsonInclude.value());
    }
}
