package testutil;

import com.tracktainment.duxmanager.document.DigitalUserDocument;
import com.tracktainment.duxmanager.domain.DigitalUser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

public class TestDigitalUserDocumentDataUtil {

    public static DigitalUserDocument createTestDigitalUserDocument() {
        return DigitalUserDocument.builder()
                .id("223e4567-e89b-12d3-a456-426614174008")
                .identityProviderInformation(
                        DigitalUserDocument.IdentityProviderInformation.builder()
                        .subject("auth2|123456")
                        .identityProvider(DigitalUser.IdentityProviderInformation.IdentityProvider.KEY_CLOAK)
                        .tenantId("tenant1")
                        .build()
                )
                .personalInformation(
                        DigitalUserDocument.PersonalInformation.builder()
                                .fullName("John Doe")
                                .firstName("John")
                                .lastName("Doe")
                                .birthDate("1990-01-01")
                                .build()
                )
                .contactMediumList(
                        Collections.singletonList(
                                DigitalUserDocument.ContactMedium.builder()
                                        .type(DigitalUser.ContactMedium.Type.EMAIL)
                                        .preferred(true)
                                        .characteristic(
                                                DigitalUserDocument.ContactMedium.Characteristic.builder()
                                                        .emailAddress("john.doe@example.com")
                                                        .build()
                                        )
                                        .build()
                        )
                )
                .assets(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now().plusDays(1))
                .build();
    }
}
