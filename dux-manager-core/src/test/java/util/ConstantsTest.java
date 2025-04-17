package util;

import com.tracktainment.duxmanager.util.Constants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ConstantsTest {

    @Test
    void shouldHavePublicConstructor() throws NoSuchMethodException {
        // Arrange
        Constructor<Constants> constructor = Constants.class.getDeclaredConstructor();

        // Assert
        assertTrue(Modifier.isPublic(constructor.getModifiers()));
    }

    @Test
    void shouldThrowExceptionWhenInstantiated() throws NoSuchMethodException {
        // Arrange
        Constructor<Constants> constructor = Constants.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // Act & Assert
        Exception exception = assertThrows(InvocationTargetException.class, constructor::newInstance);
        Throwable cause = exception.getCause();
        assertEquals(IllegalStateException.class, cause.getClass());
        assertEquals("Util class cannot be instantiated.", cause.getMessage());
    }

    @Test
    void shouldHaveCorrectDefaultValues() {
        // Assert
        assertEquals("0", Constants.DEFAULT_OFFSET);
        assertEquals("10", Constants.DEFAULT_LIMIT);
        assertEquals(0, Constants.MIN_OFFSET);
        assertEquals(1, Constants.MIN_LIMIT);
        assertEquals(100, Constants.MAX_LIMIT);
    }

    @ParameterizedTest
    @MethodSource("provideValidRegexMatches")
    void shouldMatchValidRegexPatterns(String input, String pattern) {
        // Assert
        assertTrue(input.matches(pattern), "Input '" + input + "' should match pattern '" + pattern + "'");
    }

    @ParameterizedTest
    @MethodSource("provideInvalidRegexMatches")
    void shouldNotMatchInvalidRegexPatterns(String input, String pattern) {
        // Assert
        assertFalse(input.matches(pattern), "Input '" + input + "' should not match pattern '" + pattern + "'");
    }

    @Test
    void shouldHaveCorrectErrorMessages() {
        // Assert for mandatory field messages
        assertTrue(Constants.USER_IDP_INFO_MANDATORY_MSG.contains("mandatory"));
        assertTrue(Constants.USER_PERSONAL_INFO_MANDATORY_MSG.contains("mandatory"));
        assertTrue(Constants.ASSET_EXTERNAL_ID_MANDATORY_MSG.contains("mandatory"));

        // Assert for validation error messages
        assertTrue(Constants.ID_INVALID_MSG.contains("must match"));
        assertTrue(Constants.GROUP_ID_INVALID_MSG.contains("must match"));
        assertTrue(Constants.OFFSET_INVALID_MSG.contains("must be positive"));
        assertTrue(Constants.LIMIT_INVALID_MSG.contains("range"));
    }

    private static Stream<Arguments> provideValidRegexMatches() {
        return Stream.of(
                // GROUP_ID_REGEX
                Arguments.of("com.tracktainment", Constants.GROUP_ID_REGEX),
                Arguments.of("org.example", Constants.GROUP_ID_REGEX),
                Arguments.of("my-group", Constants.GROUP_ID_REGEX),

                // ARTIFACT_ID_REGEX
                Arguments.of("test-artifact", Constants.ARTIFACT_ID_REGEX),
                Arguments.of("myArtifact", Constants.ARTIFACT_ID_REGEX),

                // VERSION_REGEX
                Arguments.of("1.0.0", Constants.VERSION_REGEX),
                Arguments.of("0.0.1-SNAPSHOT", Constants.VERSION_REGEX),

                // TYPE_REGEX
                Arguments.of("book", Constants.TYPE_REGEX),
                Arguments.of("movie", Constants.TYPE_REGEX),

                // ID_REGEX
                Arguments.of(UUID.randomUUID().toString(), Constants.ID_REGEX),

                // SUB_REGEX
                Arguments.of("auth2123456", Constants.SUB_REGEX),
                Arguments.of("google-oauth2123456789", Constants.SUB_REGEX),

                // TENANT_ID_REGEX
                Arguments.of("tenant1", Constants.TENANT_ID_REGEX),
                Arguments.of("org-123", Constants.TENANT_ID_REGEX),

                // FULL_NAME_REGEX
                Arguments.of("John Doe", Constants.FULL_NAME_REGEX),
                Arguments.of("John Michael Doe", Constants.FULL_NAME_REGEX),

                // SINGLE_NAME_REGEX
                Arguments.of("John", Constants.SINGLE_NAME_REGEX),
                Arguments.of("O'Brien", Constants.SINGLE_NAME_REGEX),

                // COUNTRY_CODE_REGEX
                Arguments.of("+1", Constants.COUNTRY_CODE_REGEX),
                Arguments.of("+44", Constants.COUNTRY_CODE_REGEX),

                // PHONE_NUMBER_REGEX
                Arguments.of("555-123-4567", Constants.PHONE_NUMBER_REGEX),
                Arguments.of("(555)123-4567", Constants.PHONE_NUMBER_REGEX),

                // EMAIL_REGEX
                Arguments.of("john.doe@example.com", Constants.EMAIL_REGEX),
                Arguments.of("user+tag@domain.co.uk", Constants.EMAIL_REGEX),

                // GENERIC_ADDRESS_REGEX
                Arguments.of("123 Main St", Constants.GENERIC_ADDRESS_REGEX),
                Arguments.of("New York", Constants.GENERIC_ADDRESS_REGEX),

                // POSTAL_CODE_REGEX
                Arguments.of("10001", Constants.POSTAL_CODE_REGEX),
                Arguments.of("AB1 2CD", Constants.POSTAL_CODE_REGEX)
        );
    }

    private static Stream<Arguments> provideInvalidRegexMatches() {
        return Stream.of(
                // GROUP_ID_REGEX - too long
                Arguments.of("a".repeat(51), Constants.GROUP_ID_REGEX),

                // ARTIFACT_ID_REGEX - invalid characters
                Arguments.of("artifact&", Constants.ARTIFACT_ID_REGEX),

                // VERSION_REGEX - invalid characters
                Arguments.of("1.0.0@", Constants.VERSION_REGEX),

                // TYPE_REGEX - too long
                Arguments.of("a".repeat(31), Constants.TYPE_REGEX),

                // ID_REGEX - not a UUID
                Arguments.of("not-a-uuid", Constants.ID_REGEX),

                // SUB_REGEX - invalid characters
                Arguments.of("auth2user@example", Constants.SUB_REGEX),

                // TENANT_ID_REGEX - invalid characters
                Arguments.of("tenant@1", Constants.TENANT_ID_REGEX),

                // FULL_NAME_REGEX - missing space
                Arguments.of("JohnDoe", Constants.FULL_NAME_REGEX),

                // SINGLE_NAME_REGEX - contains space
                Arguments.of("John Doe", Constants.SINGLE_NAME_REGEX),

                // COUNTRY_CODE_REGEX - missing plus
                Arguments.of("1", Constants.COUNTRY_CODE_REGEX),

                // PHONE_NUMBER_REGEX - invalid format
                Arguments.of("phone", Constants.PHONE_NUMBER_REGEX),

                // EMAIL_REGEX - invalid format
                Arguments.of("not-an-email", Constants.EMAIL_REGEX),

                // GENERIC_ADDRESS_REGEX - invalid characters
                Arguments.of("123 Main St €", Constants.GENERIC_ADDRESS_REGEX),

                // POSTAL_CODE_REGEX - too long
                Arguments.of("a".repeat(11), Constants.POSTAL_CODE_REGEX)
        );
    }
}
