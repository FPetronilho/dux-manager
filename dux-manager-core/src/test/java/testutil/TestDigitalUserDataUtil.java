package testutil;

import com.tracktainment.duxmanager.domain.DigitalUser;
import com.tracktainment.duxmanager.dto.DigitalUserCreate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

public class TestDigitalUserDataUtil {

    public static DigitalUserCreate createTestDigitalUserCreate(){
        return DigitalUserCreate.builder()
                .identityProviderInformation(DigitalUserCreate.IdentityProviderInformation.builder()
                        .subject("auth2|123456")
                        .identityProvider(DigitalUserCreate.IdentityProviderInformation.IdentityProvider.KEY_CLOAK)
                        .tenantId("tenant1")
                        .build()
                )
                .personalInformation(DigitalUserCreate.PersonalInformation.builder()
                        .fullName("John Doe")
                        .firstName("John")
                        .lastName("Doe")
                        .birthDate("1990-01-01")
                        .build()
                )
                .contactMediumList(Collections.singletonList(
                        DigitalUserCreate.ContactMedium.builder()
                                .type(DigitalUserCreate.ContactMedium.Type.EMAIL)
                                .preferred(true)
                                .characteristic(
                                        DigitalUserCreate.ContactMedium.Characteristic.builder()
                                                .emailAddress("john.doe@example.com")
                                                .build()
                                )
                                .build()
                        )
                )
                .build();
    }

    public static DigitalUser createTestDigitalUser(){
        return DigitalUser.builder()
                .id("223e4567-e89b-12d3-a456-426614174008")
                .identityProviderInformation(DigitalUser.IdentityProviderInformation.builder()
                        .subject("auth2|123456")
                        .identityProvider(DigitalUser.IdentityProviderInformation.IdentityProvider.KEY_CLOAK)
                        .tenantId("tenant1")
                        .build()
                )
                .personalInformation(DigitalUser.PersonalInformation.builder()
                        .fullName("John Doe")
                        .firstName("John")
                        .lastName("Doe")
                        .birthDate("1990-01-01")
                        .build()
                )
                .contactMediumList(Collections.singletonList(
                                DigitalUser.ContactMedium.builder()
                                        .type(DigitalUser.ContactMedium.Type.EMAIL)
                                        .preferred(true)
                                        .characteristic(
                                                DigitalUser.ContactMedium.Characteristic.builder()
                                                        .emailAddress("john.doe@example.com")
                                                        .build()
                                        )
                                        .build()
                        )
                )
                .assets(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
