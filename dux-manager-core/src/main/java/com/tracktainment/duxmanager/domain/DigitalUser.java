package com.tracktainment.duxmanager.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tracktainment.duxmanager.annotation.Encrypted;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Digital user information")
public class DigitalUser extends BaseObject {

    @Schema(description = "Identity provider information")
    private IdentityProviderInformation identityProviderInformation;

    @Schema(description = "Personal information")
    private PersonalInformation personalInformation;

    @Schema(description = "Contact medium list")
    private List<ContactMedium> contactMediumList;

    @Schema(description = "Assets owned by the user")
    private List<Asset> assets;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "Identity provider information")
    public static class IdentityProviderInformation {

        @Schema(description = "Subject identifier from identity provider", example = "auth2|123456789")
        private String subject;

        @Schema(description = "Identity provider")
        private IdentityProvider identityProvider;

        @Schema(description = "Tenant identifier", example = "tenant123")
        private String tenantId;

        @ToString
        @Getter
        @RequiredArgsConstructor
        @Schema(description = "Identity provider types")
        public enum IdentityProvider {

            @Schema(description = "Amazon Cognito")
            AMAZON_COGNITO("amazonCognito"),

            @Schema(description = "Apple ID")
            APPLE_ID("appleId"),

            @Schema(description = "Google Identity Platform")
            GOOGLE_IDENTITY_PLATFORM("googleIdentityPlatform"),

            @Schema(description = "KeyCloak")
            KEY_CLOAK("keyCloak"),

            @Schema(description = "Microsoft Entra ID")
            MICROSOFT_ENTRA_ID("microsoftEntraId");

            private final String value;
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "Personal information")
    public static class PersonalInformation {

        @Encrypted
        @Schema(description = "Full name", example = "John Michael Doe")
        private String fullName;

        @Encrypted
        @Schema(description = "First name", example = "John")
        private String firstName;

        @Encrypted
        @Schema(description = "Middle name", example = "Michael")
        private String middleName;

        @Encrypted
        @Schema(description = "Last name", example = "Doe")
        private String lastName;

        @Encrypted
        @Schema(description = "Nickname", example = "Johnny")
        private String nickname;

        @Encrypted
        @Schema(description = "Birth date", example = "1980-05-15")
        private LocalDate birthDate;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "Contact medium information")
    public static class ContactMedium {

        @Schema(description = "Whether this is the preferred contact method", example = "true")
        private boolean preferred;

        @Schema(description = "Contact type")
        private Type type;

        @Schema(description = "Contact characteristics")
        private Characteristic characteristic;

        @Schema(description = "Expiration date and time", example = "2025-12-31T23:59:59")
        private LocalDateTime expiresAt;

        @ToString
        @Getter
        @RequiredArgsConstructor
        @Schema(description = "Contact medium types")
        public enum Type {

            @Schema(description = "Phone contact")
            PHONE("phone"),

            @Schema(description = "Email contact")
            EMAIL("email"),

            @Schema(description = "Geographic address")
            GEOGRAPHIC_ADDRESS("geographicAddress");

            private final String value;
        }

        @AllArgsConstructor
        @NoArgsConstructor
        @Data
        @Builder
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @Schema(description = "Contact medium characteristics")
        public static class Characteristic {

            // Phone
            @Encrypted
            @Schema(description = "Country code", example = "+1")
            private String countryCode;

            @Encrypted
            @Schema(description = "Phone number", example = "555-123-4567")
            private String phoneNumber;

            // Email
            @Encrypted
            @Schema(description = "Email address", example = "john.doe@example.com")
            private String emailAddress;

            // Geographic address
            @Encrypted
            @Schema(description = "Country", example = "United States")
            private String country;

            @Encrypted
            @Schema(description = "City", example = "New York")
            private String city;

            @Encrypted
            @Schema(description = "State or province", example = "NY")
            private String stateOrProvince;

            @Encrypted
            @Schema(description = "Postal code", example = "10001")
            private String postalCode;

            @Encrypted
            @Schema(description = "Street address line 1", example = "123 Main St")
            private String street1;

            @Encrypted
            @Schema(description = "Street address line 2", example = "Apt 4B")
            private String street2;
        }
    }
}