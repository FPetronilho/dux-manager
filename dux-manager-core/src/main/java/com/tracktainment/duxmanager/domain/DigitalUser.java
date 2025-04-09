package com.tracktainment.duxmanager.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DigitalUser extends BaseObject {

    private IdentityProviderInformation identityProviderInformation;
    private PersonalInformation personalInformation;
    private List<ContactMedium> contactMediumList;
    private List<Asset> assets;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class IdentityProviderInformation {

        private String subject;
        private IdentityProvider identityProvider;
        private String tenantId;

        @ToString
        @Getter
        @RequiredArgsConstructor
        public enum IdentityProvider {

            AMAZON_COGNITO("amazonCognito"),
            APPLE_ID("appleId"),
            GOOGLE_IDENTITY_PLATFORM("googleIdentityPlatform"),
            KEY_CLOAK("keyCloak"),
            MICROSOFT_ENTRA_ID("microsoftEntraId");

            private final String value;
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PersonalInformation {

        private String fullName;
        private String firstName;
        private String middleName;
        private String lastName;
        private String nickname;
        private LocalDate birthDate;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ContactMedium {

        private boolean preferred;
        private Type type;
        private Characteristic characteristic;
        private LocalDateTime expiresAt;


        @ToString
        @Getter
        @RequiredArgsConstructor
        public enum Type {

            PHONE("phone"),
            EMAIL("email"),
            GEOGRAPHIC_ADDRESS("geographicAddress");

            private final String value;
        }

        @AllArgsConstructor
        @NoArgsConstructor
        @Data
        @Builder
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class Characteristic {

            // Phone
            private String countryCode;
            private String phoneNumber;

            // Email
            private String emailAddress;

            // Geographic address
            private String country;
            private String city;
            private String stateOrProvince;
            private String postalCode;
            private String street1;
            private String street2;
        }
    }
}
