package mapper;

import com.tracktainment.duxmanager.document.DigitalUserDocument;
import com.tracktainment.duxmanager.domain.DigitalUser;
import com.tracktainment.duxmanager.dto.DigitalUserCreate;
import com.tracktainment.duxmanager.mapper.DigitalUserMapperDataProvider;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import testutil.TestDigitalUserDataUtil;
import testutil.TestDigitalUserDocumentDataUtil;

import java.util.ArrayList;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class DigitalUserMapperDataProviderTest {

    private final DigitalUserMapperDataProvider mapper = Mappers.getMapper(DigitalUserMapperDataProvider.class);

    // UUID pattern matcher
    private static final Pattern UUID_PATTERN =
            Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

    @Test
    void shouldMapDigitalUserDocumentToDigitalUser() {
        // Arrange
        DigitalUserDocument digitalUserDocument = TestDigitalUserDocumentDataUtil.createTestDigitalUserDocument();

        // Act
        DigitalUser result = mapper.toDigitalUser(digitalUserDocument);

        // Assert
        assertNotNull(result);
        assertEquals(digitalUserDocument.getId(), result.getId());
        assertEquals(digitalUserDocument.getCreatedAt(), result.getCreatedAt());
        assertEquals(digitalUserDocument.getUpdatedAt(), result.getUpdatedAt());

        assertEquals(digitalUserDocument.getIdentityProviderInformation(), result.getIdentityProviderInformation());
        assertEquals(digitalUserDocument.getPersonalInformation(), result.getPersonalInformation());
        assertEquals(1, result.getContactMediumList().size());
        assertEquals(digitalUserDocument.getContactMediumList().get(0), result.getContactMediumList().get(0));
        assertTrue(result.getAssets().isEmpty());
    }

    @Test
    void shouldMapDigitalUserCreateToDigitalUserDocument() {
        // Arrange
        DigitalUserCreate digitalUserCreate = TestDigitalUserDataUtil.createTestDigitalUserCreate();

        // Act
        DigitalUserDocument result = mapper.toDigitalUserDocument(digitalUserCreate);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getId());
        assertTrue(UUID_PATTERN.matcher(result.getId()).matches(), "ID should be a valid UUID");

        assertNull(result.getDbId());
        assertNull(result.getCreatedAt());
        assertNull(result.getUpdatedAt());
        assertNull(result.getAssets());

        assertNotNull(result.getIdentityProviderInformation());
        assertEquals(
                digitalUserCreate.getIdentityProviderInformation().getSubject(),
                result.getIdentityProviderInformation().getSubject()
        );
        assertEquals(
                DigitalUser.IdentityProviderInformation.IdentityProvider.KEY_CLOAK,
                result.getIdentityProviderInformation().getIdentityProvider()
        );
        assertEquals(
                digitalUserCreate.getIdentityProviderInformation().getTenantId(),
                result.getIdentityProviderInformation().getTenantId()
        );

        assertNotNull(result.getPersonalInformation());
        assertEquals(
                digitalUserCreate.getPersonalInformation().getFullName(),
                result.getPersonalInformation().getFullName()
        );
        assertEquals(
                digitalUserCreate.getPersonalInformation().getFirstName(),
                result.getPersonalInformation().getFirstName()
        );
        assertEquals(
                digitalUserCreate.getPersonalInformation().getLastName(),
                result.getPersonalInformation().getLastName()
        );
        assertEquals(
                digitalUserCreate.getPersonalInformation().getBirthDate(),
                result.getPersonalInformation().getBirthDate()
        );

        assertNotNull(result.getContactMediumList());
        assertEquals(1, result.getContactMediumList().size());
        assertEquals(
                digitalUserCreate.getContactMediumList().get(0).isPreferred(),
                result.getContactMediumList().get(0).isPreferred()
        );
        assertEquals(
                DigitalUser.ContactMedium.Type.EMAIL,
                result.getContactMediumList().get(0).getType()
        );
        assertEquals(
                digitalUserCreate.getContactMediumList().get(0).getCharacteristic().getEmailAddress(),
                result.getContactMediumList().get(0).getCharacteristic().getEmailAddress()
        );
    }

    @Test
    void shouldHandleNullInputs() {
        // Act & Assert
        assertNull(mapper.toDigitalUser(null));
        assertNull(mapper.toDigitalUserDocument(null));
    }

    @Test
    void shouldGenerateDifferentIdsForDifferentCalls() {
        // Arrange
        DigitalUserCreate digitalUserCreate1 = DigitalUserCreate.builder()
                .identityProviderInformation(DigitalUserCreate.IdentityProviderInformation.builder()
                        .subject("subject1")
                        .build())
                .build();

        DigitalUserCreate digitalUserCreate2 = DigitalUserCreate.builder()
                .identityProviderInformation(DigitalUserCreate.IdentityProviderInformation.builder()
                        .subject("subject2")
                        .build())
                .build();

        // Act
        DigitalUserDocument result1 = mapper.toDigitalUserDocument(digitalUserCreate1);
        DigitalUserDocument result2 = mapper.toDigitalUserDocument(digitalUserCreate2);

        // Assert
        assertNotNull(result1.getId());
        assertNotNull(result2.getId());
        assertNotEquals(result1.getId(), result2.getId(), "Generated IDs should be different");
    }

    @Test
    void shouldMapEmptyDigitalUserCreate() {
        // Arrange
        DigitalUserCreate emptyCreate = new DigitalUserCreate();

        // Act
        DigitalUserDocument result = mapper.toDigitalUserDocument(emptyCreate);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getId());
        assertTrue(UUID_PATTERN.matcher(result.getId()).matches(), "ID should be a valid UUID");
        assertNull(result.getIdentityProviderInformation());
        assertNull(result.getPersonalInformation());
        assertNull(result.getContactMediumList());
        assertNull(result.getAssets());
        assertNull(result.getCreatedAt());
        assertNull(result.getUpdatedAt());
        assertNull(result.getDbId());
    }

    @Test
    void shouldMapEmptyDigitalUserDocument() {
        // Arrange
        DigitalUserDocument emptyDocument = new DigitalUserDocument();

        // Act
        DigitalUser result = mapper.toDigitalUser(emptyDocument);

        // Assert
        assertNotNull(result);
        assertNull(result.getId());
        assertNull(result.getIdentityProviderInformation());
        assertNull(result.getPersonalInformation());
        assertNull(result.getContactMediumList());
        assertEquals(result.getAssets(), new ArrayList<>());
        assertNull(result.getCreatedAt());
        assertNull(result.getUpdatedAt());
    }
}
