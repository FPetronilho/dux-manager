package domain;

import com.tracktainment.duxmanager.domain.Asset;
import com.tracktainment.duxmanager.domain.BaseObject;
import com.tracktainment.duxmanager.domain.DigitalUser;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DigitalUserTest {

    @Test
    void shouldCreateDigitalUserUsingBuilder() {
        // Arrange
        String id = UUID.randomUUID().toString();
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now().plusDays(1);

        DigitalUser.IdentityProviderInformation idpInfo = DigitalUser.IdentityProviderInformation.builder()
                .subject("auth0|123456")
                .identityProvider(DigitalUser.IdentityProviderInformation.IdentityProvider.KEY_CLOAK)
                .tenantId("tenant1")
                .build();

        DigitalUser.PersonalInformation personalInfo = DigitalUser.PersonalInformation.builder()
                .fullName("John Doe")
                .firstName("John")
                .middleName("Michael")
                .lastName("Doe")
                .nickname("Johnny")
                .birthDate("1990-01-01")
                .build();

        DigitalUser.ContactMedium.Characteristic emailChar = DigitalUser.ContactMedium.Characteristic.builder()
                .emailAddress("john.doe@example.com")
                .build();

        DigitalUser.ContactMedium contactMedium = DigitalUser.ContactMedium.builder()
                .type(DigitalUser.ContactMedium.Type.EMAIL)
                .preferred(true)
                .characteristic(emailChar)
                .expiresAt(LocalDateTime.now().plusYears(1))
                .build();

        Asset asset = Asset.builder()
                .id(UUID.randomUUID().toString())
                .externalId(UUID.randomUUID().toString())
                .type("book")
                .permissionPolicy(Asset.PermissionPolicy.OWNER)
                .build();

        // Act
        DigitalUser digitalUser = DigitalUser.builder()
                .id(id)
                .identityProviderInformation(idpInfo)
                .personalInformation(personalInfo)
                .contactMediumList(Collections.singletonList(contactMedium))
                .assets(Collections.singletonList(asset))
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        // Assert
        assertEquals(id, digitalUser.getId());
        assertEquals(idpInfo, digitalUser.getIdentityProviderInformation());
        assertEquals(personalInfo, digitalUser.getPersonalInformation());
        assertEquals(1, digitalUser.getContactMediumList().size());
        assertEquals(contactMedium, digitalUser.getContactMediumList().get(0));
        assertEquals(1, digitalUser.getAssets().size());
        assertEquals(asset, digitalUser.getAssets().get(0));
        assertEquals(createdAt, digitalUser.getCreatedAt());
        assertEquals(updatedAt, digitalUser.getUpdatedAt());
    }

    @Test
    void shouldCreateEmptyDigitalUserUsingNoArgsConstructor() {
        // Act
        DigitalUser digitalUser = new DigitalUser();

        // Assert
        assertNull(digitalUser.getId());
        assertNull(digitalUser.getIdentityProviderInformation());
        assertNull(digitalUser.getPersonalInformation());
        assertNull(digitalUser.getContactMediumList());
        assertNull(digitalUser.getAssets());
        assertNull(digitalUser.getCreatedAt());
        assertNull(digitalUser.getUpdatedAt());
    }

    @Test
    void shouldInheritFromBaseObject() {
        // Arrange
        DigitalUser digitalUser = new DigitalUser();

        // Assert
        assertTrue(digitalUser instanceof BaseObject);
    }

    @Test
    void shouldImplementEqualsAndHashCodeCorrectly() {
        // Arrange
        String id1 = UUID.randomUUID().toString();
        String id2 = UUID.randomUUID().toString();

        DigitalUser user1 = DigitalUser.builder().id(id1).build();
        DigitalUser user2 = DigitalUser.builder().id(id1).build(); // Same ID
        DigitalUser user3 = DigitalUser.builder().id(id2).build(); // Different ID

        // Assert
        assertEquals(user1, user2); // Same ID should be equal
        assertNotEquals(user1, user3); // Different ID should not be equal
        assertEquals(user1.hashCode(), user2.hashCode()); // Same ID should have same hashCode
        assertNotEquals(user1.hashCode(), user3.hashCode()); // Different ID should have different hashCode
    }

    @Test
    void shouldImplementToString() {
        // Arrange
        DigitalUser digitalUser = DigitalUser.builder()
                .id(UUID.randomUUID().toString())
                .build();

        // Act
        String toString = digitalUser.toString();

        // Assert
        assertNotNull(toString);
        assertFalse(toString.isEmpty());
        assertTrue(toString.contains("DigitalUser"));
    }

    @Test
    void shouldCreateIdentityProviderInformationUsingBuilder() {
        // Arrange
        String subject = "auth0|123456";
        DigitalUser.IdentityProviderInformation.IdentityProvider idp =
                DigitalUser.IdentityProviderInformation.IdentityProvider.KEY_CLOAK;
        String tenantId = "tenant1";

        // Act
        DigitalUser.IdentityProviderInformation idpInfo = DigitalUser.IdentityProviderInformation.builder()
                .subject(subject)
                .identityProvider(idp)
                .tenantId(tenantId)
                .build();

        // Assert
        assertEquals(subject, idpInfo.getSubject());
        assertEquals(idp, idpInfo.getIdentityProvider());
        assertEquals(tenantId, idpInfo.getTenantId());
    }

    @Test
    void shouldCreatePersonalInformationUsingBuilder() {
        // Arrange
        String fullName = "John Michael Doe";
        String firstName = "John";
        String middleName = "Michael";
        String lastName = "Doe";
        String nickname = "Johnny";
        String birthDate = "1990-01-01";

        // Act
        DigitalUser.PersonalInformation personalInfo = DigitalUser.PersonalInformation.builder()
                .fullName(fullName)
                .firstName(firstName)
                .middleName(middleName)
                .lastName(lastName)
                .nickname(nickname)
                .birthDate(birthDate)
                .build();

        // Assert
        assertEquals(fullName, personalInfo.getFullName());
        assertEquals(firstName, personalInfo.getFirstName());
        assertEquals(middleName, personalInfo.getMiddleName());
        assertEquals(lastName, personalInfo.getLastName());
        assertEquals(nickname, personalInfo.getNickname());
        assertEquals(birthDate, personalInfo.getBirthDate());
    }

    @Test
    void shouldCreateContactMediumUsingBuilder() {
        // Arrange
        boolean preferred = true;
        DigitalUser.ContactMedium.Type type = DigitalUser.ContactMedium.Type.EMAIL;
        LocalDateTime expiresAt = LocalDateTime.now().plusYears(1);

        DigitalUser.ContactMedium.Characteristic characteristic = DigitalUser.ContactMedium.Characteristic.builder()
                .emailAddress("john.doe@example.com")
                .build();

        // Act
        DigitalUser.ContactMedium contactMedium = DigitalUser.ContactMedium.builder()
                .preferred(preferred)
                .type(type)
                .characteristic(characteristic)
                .expiresAt(expiresAt)
                .build();

        // Assert
        assertEquals(preferred, contactMedium.isPreferred());
        assertEquals(type, contactMedium.getType());
        assertEquals(characteristic, contactMedium.getCharacteristic());
        assertEquals(expiresAt, contactMedium.getExpiresAt());
    }

    @Test
    void shouldCreateContactMediumCharacteristicUsingBuilder() {
        // Arrange
        String countryCode = "+1";
        String phoneNumber = "555-123-4567";
        String emailAddress = "john.doe@example.com";
        String country = "United States";
        String city = "New York";
        String stateOrProvince = "NY";
        String postalCode = "10001";
        String street1 = "123 Main St";
        String street2 = "Apt 4B";

        // Act
        DigitalUser.ContactMedium.Characteristic characteristic = DigitalUser.ContactMedium.Characteristic.builder()
                .countryCode(countryCode)
                .phoneNumber(phoneNumber)
                .emailAddress(emailAddress)
                .country(country)
                .city(city)
                .stateOrProvince(stateOrProvince)
                .postalCode(postalCode)
                .street1(street1)
                .street2(street2)
                .build();

        // Assert
        assertEquals(countryCode, characteristic.getCountryCode());
        assertEquals(phoneNumber, characteristic.getPhoneNumber());
        assertEquals(emailAddress, characteristic.getEmailAddress());
        assertEquals(country, characteristic.getCountry());
        assertEquals(city, characteristic.getCity());
        assertEquals(stateOrProvince, characteristic.getStateOrProvince());
        assertEquals(postalCode, characteristic.getPostalCode());
        assertEquals(street1, characteristic.getStreet1());
        assertEquals(street2, characteristic.getStreet2());
    }

    @Test
    void identityProviderEnumShouldHaveCorrectValues() {
        // Assert
        assertEquals("amazonCognito", DigitalUser.IdentityProviderInformation.IdentityProvider.AMAZON_COGNITO.getValue());
        assertEquals("appleId", DigitalUser.IdentityProviderInformation.IdentityProvider.APPLE_ID.getValue());
        assertEquals("googleIdentityPlatform", DigitalUser.IdentityProviderInformation.IdentityProvider.GOOGLE_IDENTITY_PLATFORM.getValue());
        assertEquals("keyCloak", DigitalUser.IdentityProviderInformation.IdentityProvider.KEY_CLOAK.getValue());
        assertEquals("microsoftEntraId", DigitalUser.IdentityProviderInformation.IdentityProvider.MICROSOFT_ENTRA_ID.getValue());
        assertEquals(5, DigitalUser.IdentityProviderInformation.IdentityProvider.values().length);
    }

    @Test
    void contactMediumTypeShouldHaveCorrectValues() {
        // Assert
        assertEquals("phone", DigitalUser.ContactMedium.Type.PHONE.getValue());
        assertEquals("email", DigitalUser.ContactMedium.Type.EMAIL.getValue());
        assertEquals("geographicAddress", DigitalUser.ContactMedium.Type.GEOGRAPHIC_ADDRESS.getValue());
        assertEquals(3, DigitalUser.ContactMedium.Type.values().length);
    }
}
