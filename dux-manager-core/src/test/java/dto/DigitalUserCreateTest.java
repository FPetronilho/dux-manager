package dto;

import com.tracktainment.duxmanager.dto.DigitalUserCreate;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class DigitalUserCreateTest {

    @Test
    void shouldCreateDigitalUserCreateUsingBuilder() {
        // Arrange
        DigitalUserCreate.IdentityProviderInformation idpInfo = DigitalUserCreate.IdentityProviderInformation.builder()
                .subject("auth0|123456")
                .identityProvider(DigitalUserCreate.IdentityProviderInformation.IdentityProvider.KEY_CLOAK)
                .tenantId("tenant1")
                .build();

        DigitalUserCreate.PersonalInformation personalInfo = DigitalUserCreate.PersonalInformation.builder()
                .fullName("John Doe")
                .firstName("John")
                .middleName("Michael")
                .lastName("Doe")
                .nickname("Johnny")
                .birthDate(LocalDate.of(1990, 1, 1))
                .build();

        DigitalUserCreate.ContactMedium.Characteristic emailChar = DigitalUserCreate.ContactMedium.Characteristic.builder()
                .emailAddress("john.doe@example.com")
                .build();

        DigitalUserCreate.ContactMedium contactMedium = DigitalUserCreate.ContactMedium.builder()
                .type(DigitalUserCreate.ContactMedium.Type.EMAIL)
                .preferred(true)
                .characteristic(emailChar)
                .expiresAt(LocalDateTime.now().plusYears(1))
                .build();

        // Act
        DigitalUserCreate digitalUserCreate = DigitalUserCreate.builder()
                .identityProviderInformation(idpInfo)
                .personalInformation(personalInfo)
                .contactMediumList(Collections.singletonList(contactMedium))
                .build();

        // Assert
        assertEquals(idpInfo, digitalUserCreate.getIdentityProviderInformation());
        assertEquals(personalInfo, digitalUserCreate.getPersonalInformation());
        assertEquals(1, digitalUserCreate.getContactMediumList().size());
        assertEquals(contactMedium, digitalUserCreate.getContactMediumList().get(0));
    }

    @Test
    void shouldCreateEmptyDigitalUserCreateUsingNoArgsConstructor() {
        // Act
        DigitalUserCreate digitalUserCreate = new DigitalUserCreate();

        // Assert
        assertNull(digitalUserCreate.getIdentityProviderInformation());
        assertNull(digitalUserCreate.getPersonalInformation());
        assertNull(digitalUserCreate.getContactMediumList());
    }

    @Test
    void shouldUseSettersAndGetters() {
        // Arrange
        DigitalUserCreate digitalUserCreate = new DigitalUserCreate();

        DigitalUserCreate.IdentityProviderInformation idpInfo = DigitalUserCreate.IdentityProviderInformation.builder()
                .subject("auth0|123456")
                .identityProvider(DigitalUserCreate.IdentityProviderInformation.IdentityProvider.KEY_CLOAK)
                .tenantId("tenant1")
                .build();

        DigitalUserCreate.PersonalInformation personalInfo = DigitalUserCreate.PersonalInformation.builder()
                .fullName("John Doe")
                .build();

        DigitalUserCreate.ContactMedium contactMedium = DigitalUserCreate.ContactMedium.builder()
                .type(DigitalUserCreate.ContactMedium.Type.EMAIL)
                .build();

        // Act
        digitalUserCreate.setIdentityProviderInformation(idpInfo);
        digitalUserCreate.setPersonalInformation(personalInfo);
        digitalUserCreate.setContactMediumList(Collections.singletonList(contactMedium));

        // Assert
        assertEquals(idpInfo, digitalUserCreate.getIdentityProviderInformation());
        assertEquals(personalInfo, digitalUserCreate.getPersonalInformation());
        assertEquals(1, digitalUserCreate.getContactMediumList().size());
        assertEquals(contactMedium, digitalUserCreate.getContactMediumList().get(0));
    }

    @Test
    void shouldImplementToString() {
        // Arrange
        DigitalUserCreate digitalUserCreate = DigitalUserCreate.builder()
                .identityProviderInformation(DigitalUserCreate.IdentityProviderInformation.builder()
                        .subject("auth0|123456")
                        .build())
                .build();

        // Act
        String toString = digitalUserCreate.toString();

        // Assert
        assertNotNull(toString);
        assertFalse(toString.isEmpty());
        assertTrue(toString.contains("DigitalUserCreate"));
    }

    @Test
    void shouldCreateIdentityProviderInformationUsingBuilder() {
        // Arrange
        String subject = "auth0|123456";
        DigitalUserCreate.IdentityProviderInformation.IdentityProvider idp =
                DigitalUserCreate.IdentityProviderInformation.IdentityProvider.KEY_CLOAK;
        String tenantId = "tenant1";

        // Act
        DigitalUserCreate.IdentityProviderInformation idpInfo = DigitalUserCreate.IdentityProviderInformation.builder()
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
        LocalDate birthDate = LocalDate.of(1990, 1, 1);

        // Act
        DigitalUserCreate.PersonalInformation personalInfo = DigitalUserCreate.PersonalInformation.builder()
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
        DigitalUserCreate.ContactMedium.Type type = DigitalUserCreate.ContactMedium.Type.EMAIL;
        LocalDateTime expiresAt = LocalDateTime.now().plusYears(1);

        DigitalUserCreate.ContactMedium.Characteristic characteristic = DigitalUserCreate.ContactMedium.Characteristic.builder()
                .emailAddress("john.doe@example.com")
                .build();

        // Act
        DigitalUserCreate.ContactMedium contactMedium = DigitalUserCreate.ContactMedium.builder()
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
        DigitalUserCreate.ContactMedium.Characteristic characteristic = DigitalUserCreate.ContactMedium.Characteristic.builder()
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
        assertEquals("amazonCognito", DigitalUserCreate.IdentityProviderInformation.IdentityProvider.AMAZON_COGNITO.getValue());
        assertEquals("appleId", DigitalUserCreate.IdentityProviderInformation.IdentityProvider.APPLE_ID.getValue());
        assertEquals("googleIdentityPlatform", DigitalUserCreate.IdentityProviderInformation.IdentityProvider.GOOGLE_IDENTITY_PLATFORM.getValue());
        assertEquals("keyCloak", DigitalUserCreate.IdentityProviderInformation.IdentityProvider.KEY_CLOAK.getValue());
        assertEquals("microsoftEntraId", DigitalUserCreate.IdentityProviderInformation.IdentityProvider.MICROSOFT_ENTRA_ID.getValue());
        assertEquals(5, DigitalUserCreate.IdentityProviderInformation.IdentityProvider.values().length);
    }

    @Test
    void contactMediumTypeShouldHaveCorrectValues() {
        // Assert
        assertEquals("phone", DigitalUserCreate.ContactMedium.Type.PHONE.getValue());
        assertEquals("email", DigitalUserCreate.ContactMedium.Type.EMAIL.getValue());
        assertEquals("geographicAddress", DigitalUserCreate.ContactMedium.Type.GEOGRAPHIC_ADDRESS.getValue());
        assertEquals(3, DigitalUserCreate.ContactMedium.Type.values().length);
    }

    @Test
    void contactMediumTypeShouldHaveCorrectToString() {
        // Assert
        assertTrue(DigitalUserCreate.ContactMedium.Type.PHONE.toString().contains("PHONE"));
        assertTrue(DigitalUserCreate.ContactMedium.Type.EMAIL.toString().contains("EMAIL"));
        assertTrue(DigitalUserCreate.ContactMedium.Type.GEOGRAPHIC_ADDRESS.toString().contains("GEOGRAPHIC_ADDRESS"));
    }

    @Test
    void identityProviderShouldHaveCorrectToString() {
        // Assert
        assertTrue(DigitalUserCreate.IdentityProviderInformation.IdentityProvider.AMAZON_COGNITO.toString().contains("AMAZON_COGNITO"));
        assertTrue(DigitalUserCreate.IdentityProviderInformation.IdentityProvider.APPLE_ID.toString().contains("APPLE_ID"));
        assertTrue(DigitalUserCreate.IdentityProviderInformation.IdentityProvider.GOOGLE_IDENTITY_PLATFORM.toString().contains("GOOGLE_IDENTITY_PLATFORM"));
        assertTrue(DigitalUserCreate.IdentityProviderInformation.IdentityProvider.KEY_CLOAK.toString().contains("KEY_CLOAK"));
        assertTrue(DigitalUserCreate.IdentityProviderInformation.IdentityProvider.MICROSOFT_ENTRA_ID.toString().contains("MICROSOFT_ENTRA_ID"));
    }
}
