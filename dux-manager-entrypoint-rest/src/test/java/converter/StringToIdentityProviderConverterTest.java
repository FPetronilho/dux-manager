package converter;

import com.tracktainment.duxmanager.converter.StringToIdentityProviderConverter;
import com.tracktainment.duxmanager.domain.DigitalUser.IdentityProviderInformation.IdentityProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class StringToIdentityProviderConverterTest {

    private final StringToIdentityProviderConverter converter = new StringToIdentityProviderConverter();

    @ParameterizedTest
    @MethodSource("provideValidValues")
    void shouldConvertValidStringToIdentityProvider(String value, IdentityProvider expected) {
        // Act
        IdentityProvider result = converter.convert(value);

        // Assert
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void shouldReturnNullForNullOrEmptyString(String value) {
        // Act
        IdentityProvider result = converter.convert(value);

        // Assert
        assertNull(result);
    }

    @Test
    void shouldThrowExceptionForInvalidValue() {
        // Arrange
        String invalidValue = "invalidValue";

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> converter.convert(invalidValue));
        assertEquals("Invalid identity provider value: invalidValue", exception.getMessage());
    }

    private static Stream<Arguments> provideValidValues() {
        return Stream.of(
                Arguments.of("keyCloak", IdentityProvider.KEY_CLOAK),
                Arguments.of("amazonCognito", IdentityProvider.AMAZON_COGNITO),
                Arguments.of("appleId", IdentityProvider.APPLE_ID),
                Arguments.of("googleIdentityPlatform", IdentityProvider.GOOGLE_IDENTITY_PLATFORM),
                Arguments.of("microsoftEntraId", IdentityProvider.MICROSOFT_ENTRA_ID)
        );
    }
}
