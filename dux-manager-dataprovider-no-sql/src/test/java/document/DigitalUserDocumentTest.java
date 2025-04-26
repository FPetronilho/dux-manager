package document;

import com.tracktainment.duxmanager.annotation.Encrypted;
import com.tracktainment.duxmanager.document.BaseDocument;
import com.tracktainment.duxmanager.document.DigitalUserDocument;
import com.tracktainment.duxmanager.domain.Asset;
import com.tracktainment.duxmanager.domain.DigitalUser;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import testutil.TestAssetDataUtil;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DigitalUserDocumentTest {

    @Test
    void shouldCreateDigitalUserDocumentUsingBuilder() {
        // Arrange
        String id = UUID.randomUUID().toString();
        ObjectId dbId = new ObjectId();
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now().plusDays(1);

        DigitalUserDocument.IdentityProviderInformation idpInfo = DigitalUserDocument.IdentityProviderInformation.builder()
                .subject("auth2|123456")
                .identityProvider(DigitalUser.IdentityProviderInformation.IdentityProvider.KEY_CLOAK)
                .tenantId("tenant1")
                .build();

        DigitalUserDocument.PersonalInformation personalInfo = DigitalUserDocument.PersonalInformation.builder()
                .fullName("John Doe")
                .firstName("John")
                .lastName("Doe")
                .birthDate("1990-01-01")
                .build();

        DigitalUserDocument.ContactMedium.Characteristic emailChar = DigitalUserDocument.ContactMedium.Characteristic.builder()
                .emailAddress("john.doe@example.com")
                .build();

        DigitalUserDocument.ContactMedium contactMedium = DigitalUserDocument.ContactMedium.builder()
                .type(DigitalUser.ContactMedium.Type.EMAIL)
                .preferred(true)
                .characteristic(emailChar)
                .build();

        List<DigitalUserDocument.ContactMedium> contactMediumList = new ArrayList<>();
        contactMediumList.add(contactMedium);

        Asset asset = Asset.builder()
                .id(UUID.randomUUID().toString())
                .externalId(UUID.randomUUID().toString())
                .type("book")
                .build();

        List<Asset> assets = new ArrayList<>();
        assets.add(asset);

        // Act
        DigitalUserDocument document = DigitalUserDocument.builder()
                .id(id)
                .dbId(dbId)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .identityProviderInformation(idpInfo)
                .personalInformation(personalInfo)
                .contactMediumList(contactMediumList)
                .assets(assets)
                .build();

        // Assert
        assertEquals(id, document.getId());
        assertEquals(dbId, document.getDbId());
        assertEquals(createdAt, document.getCreatedAt());
        assertEquals(updatedAt, document.getUpdatedAt());
        assertEquals(idpInfo, document.getIdentityProviderInformation());
        assertEquals(personalInfo, document.getPersonalInformation());
        assertEquals(contactMediumList, document.getContactMediumList());
        assertEquals(assets, document.getAssets());
    }

    @Test
    void shouldCreateEmptyDigitalUserDocumentUsingNoArgsConstructor() {
        // Act
        DigitalUserDocument document = new DigitalUserDocument();

        // Assert
        assertNull(document.getId());
        assertNull(document.getDbId());
        assertNull(document.getCreatedAt());
        assertNull(document.getUpdatedAt());
        assertNull(document.getIdentityProviderInformation());
        assertNull(document.getPersonalInformation());
        assertNull(document.getContactMediumList());
        assertEquals(document.getAssets(), new ArrayList<>());
    }

    @Test
    void shouldCreateDigitalUserDocumentUsingAllArgsConstructor() {
        // Arrange
        String id = UUID.randomUUID().toString();
        ObjectId dbId = new ObjectId();
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now().plusDays(1);

        DigitalUserDocument.IdentityProviderInformation idpInfo = DigitalUserDocument.IdentityProviderInformation.builder()
                .subject("auth2|123456")
                .identityProvider(DigitalUser.IdentityProviderInformation.IdentityProvider.KEY_CLOAK)
                .tenantId("tenant1")
                .build();

        DigitalUserDocument.PersonalInformation personalInfo = DigitalUserDocument.PersonalInformation.builder()
                .fullName("John Doe")
                .build();

        List<DigitalUserDocument.ContactMedium> contactMediumList = new ArrayList<>();
        List<Asset> assets = new ArrayList<>();

        // Act
        DigitalUserDocument document = new DigitalUserDocument(
                id,
                idpInfo,
                personalInfo,
                contactMediumList,
                assets
        );

        document.setDbId(dbId);
        document.setCreatedAt(createdAt);
        document.setUpdatedAt(updatedAt);


        // Assert
        assertEquals(id, document.getId());
        assertEquals(dbId, document.getDbId());
        assertEquals(createdAt, document.getCreatedAt());
        assertEquals(updatedAt, document.getUpdatedAt());
        assertEquals(idpInfo, document.getIdentityProviderInformation());
        assertEquals(personalInfo, document.getPersonalInformation());
        assertEquals(contactMediumList, document.getContactMediumList());
        assertEquals(assets, document.getAssets());
    }

    @Test
    void shouldUseSettersAndGetters() {
        // Arrange
        DigitalUserDocument document = new DigitalUserDocument();
        String id = UUID.randomUUID().toString();
        ObjectId dbId = new ObjectId();
        LocalDateTime now = LocalDateTime.now();

        DigitalUserDocument.IdentityProviderInformation idpInfo = DigitalUserDocument.IdentityProviderInformation.builder()
                .subject("auth0|123456")
                .build();

        DigitalUserDocument.PersonalInformation personalInfo = DigitalUserDocument.PersonalInformation.builder()
                .fullName("John Doe")
                .build();

        List<DigitalUserDocument.ContactMedium> contactMediumList = new ArrayList<>();
        List<Asset> assets = new ArrayList<>();

        // Act
        document.setId(id);
        document.setDbId(dbId);
        document.setCreatedAt(now);
        document.setUpdatedAt(now);
        document.setIdentityProviderInformation(idpInfo);
        document.setPersonalInformation(personalInfo);
        document.setContactMediumList(contactMediumList);
        document.setAssets(assets);

        // Assert
        assertEquals(id, document.getId());
        assertEquals(dbId, document.getDbId());
        assertEquals(now, document.getCreatedAt());
        assertEquals(now, document.getUpdatedAt());
        assertEquals(idpInfo, document.getIdentityProviderInformation());
        assertEquals(personalInfo, document.getPersonalInformation());
        assertEquals(contactMediumList, document.getContactMediumList());
        assertEquals(assets, document.getAssets());
    }

    @Test
    void shouldInitializeAssetsWithEmptyListWhenNotProvided() {
        // Arrange & Act
        DigitalUserDocument document = new DigitalUserDocument();

        // Assert
        assertNotNull(document.getAssets());
        assertTrue(document.getAssets().isEmpty());
    }

    @Test
    void shouldInheritFromBaseDocument() {
        // Arrange
        DigitalUserDocument document = new DigitalUserDocument();

        // Assert
        assertTrue(document instanceof BaseDocument);
    }

    @Test
    void shouldImplementEqualsAndHashCodeCorrectly() {
        // Arrange
        String id1 = UUID.randomUUID().toString();
        String id2 = UUID.randomUUID().toString();

        DigitalUserDocument doc1 = DigitalUserDocument.builder().id(id1).build();
        DigitalUserDocument doc2 = DigitalUserDocument.builder().id(id1).build(); // Same ID
        DigitalUserDocument doc3 = DigitalUserDocument.builder().id(id2).build(); // Different ID

        // Assert
        assertEquals(doc1, doc1); // Same object reference
        assertEquals(doc1, doc2); // Equal by ID
        assertNotEquals(doc1, doc3); // Different ID
        assertEquals(doc1.hashCode(), doc2.hashCode()); // Same hash code for equal objects
        assertNotEquals(doc1.hashCode(), doc3.hashCode()); // Different hash code for different objects
        assertNotEquals(doc1, null); // Not equal to null
        assertNotEquals(doc1, "not a DigitalUserDocument"); // Not equal to different type
    }

    @Test
    void shouldImplementToString() {
        // Arrange
        String id = UUID.randomUUID().toString();
        DigitalUserDocument document = DigitalUserDocument.builder()
                .id(id)
                .build();

        // Act
        String toString = document.toString();

        // Assert
        assertNotNull(toString);
        assertFalse(toString.isEmpty());
        assertTrue(toString.contains("DigitalUserDocument"));
        assertTrue(toString.contains(id));
    }

    @Test
    void shouldManageAssetsCollection() {
        // Arrange
        DigitalUserDocument document = new DigitalUserDocument();
        Asset asset1 = TestAssetDataUtil.createTestAsset1();
        Asset asset2 = TestAssetDataUtil.createTestAsset2();

        // Act - Add assets
        document.getAssets().add(asset1);
        document.getAssets().add(asset2);

        // Assert
        assertEquals(2, document.getAssets().size());
        assertTrue(document.getAssets().contains(asset1));
        assertTrue(document.getAssets().contains(asset2));

        // Act - Remove an asset
        document.getAssets().remove(asset1);

        // Assert
        assertEquals(1, document.getAssets().size());
        assertFalse(document.getAssets().contains(asset1));
        assertTrue(document.getAssets().contains(asset2));

        // Act - Clear assets
        document.getAssets().clear();

        // Assert
        assertTrue(document.getAssets().isEmpty());
    }

    @Test
    void shouldReplaceAssetsList() {
        // Arrange
        DigitalUserDocument document = new DigitalUserDocument();
        Asset asset1 = TestAssetDataUtil.createTestAsset1();
        Asset asset2 = TestAssetDataUtil.createTestAsset2();
        document.getAssets().add(asset1);

        // Act
        List<Asset> newAssets = Arrays.asList(asset2);
        document.setAssets(newAssets);

        // Assert
        assertEquals(1, document.getAssets().size());
        assertFalse(document.getAssets().contains(asset1));
        assertTrue(document.getAssets().contains(asset2));
    }

    @Test
    void shouldHandleMongoIndexAnnotation() {
        try {
            // Check that the id field has the @Indexed annotation
            java.lang.reflect.Field idField = DigitalUserDocument.class.getDeclaredField("id");
            org.springframework.data.mongodb.core.index.Indexed indexedAnnotation =
                    idField.getAnnotation(org.springframework.data.mongodb.core.index.Indexed.class);

            assertNotNull(indexedAnnotation, "id field should have @Indexed annotation");
            assertTrue(indexedAnnotation.unique(), "id field should be uniquely indexed");

        } catch (NoSuchFieldException e) {
            fail("Could not find 'id' field in DigitalUserDocument class: " + e.getMessage());
        }
    }

    @Test
    void shouldHaveCorrectCollectionName() {
        org.springframework.data.mongodb.core.mapping.Document documentAnnotation =
                DigitalUserDocument.class.getAnnotation(org.springframework.data.mongodb.core.mapping.Document.class);

        assertNotNull(documentAnnotation, "DigitalUserDocument should have @Document annotation");
        assertEquals("digital-users", documentAnnotation.collection(),
                "DigitalUserDocument should map to 'digital-users' collection");
    }

    @Test
    void shouldHaveEncryptedAnnotationOnSensitiveFields() throws Exception {
        Field fullNameField = DigitalUserDocument.PersonalInformation.class.getDeclaredField("fullName");
        Field firstNameField = DigitalUserDocument.PersonalInformation.class.getDeclaredField("firstName");

        assertTrue(fullNameField.isAnnotationPresent(Encrypted.class),
                "fullName field should be annotated with @Encrypted");
        assertTrue(firstNameField.isAnnotationPresent(Encrypted.class),
                "firstName field should be annotated with @Encrypted");

        Field emailAddressField = DigitalUserDocument.ContactMedium.Characteristic.class.getDeclaredField("emailAddress");
        Field phoneNumberField = DigitalUserDocument.ContactMedium.Characteristic.class.getDeclaredField("phoneNumber");

        assertTrue(emailAddressField.isAnnotationPresent(Encrypted.class),
                "emailAddress field should be annotated with @Encrypted");
        assertTrue(phoneNumberField.isAnnotationPresent(Encrypted.class),
                "phoneNumber field should be annotated with @Encrypted");
    }
}
