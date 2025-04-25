package com.tracktainment.duxmanager.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tracktainment.duxmanager.util.Constants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Data for creating a new digital user")
public class DigitalUserCreate {

    @Schema(description = "Identity provider information")
    private IdentityProviderInformation identityProviderInformation;

    @Schema(description = "Personal information")
    private PersonalInformation personalInformation;

    @Schema(description = "Contact medium list")
    private List<ContactMedium> contactMediumList;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "Identity provider information")
    public static class IdentityProviderInformation {

        @NotNull(message = Constants.USER_SUBJECT_MANDATORY_MSG)
        @Pattern(regexp = Constants.SUB_REGEX, message = Constants.SUB_INVALID_MSG)
        @Schema(description = "Subject identifier from identity provider", example = "auth0|123456789")
        private String subject;

        @Schema(description = "Identity provider")
        private IdentityProvider identityProvider;

        @NotNull(message = Constants.USER_TENANT_ID_MANDATORY_MSG)
        @Pattern(regexp = Constants.TENANT_ID_REGEX, message = Constants.TENANT_ID_INVALID_MSG)
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

        @Pattern(regexp = Constants.FULL_NAME_REGEX, message = Constants.FULL_NAME_INVALID_MSG)
        @Schema(description = "Full name", example = "John Doe")
        private String fullName;

        @Pattern(regexp = Constants.SINGLE_NAME_REGEX, message = Constants.FIRST_NAME_INVALID_MSG)
        @Schema(description = "First name", example = "John")
        private String firstName;

        @Pattern(regexp = Constants.SINGLE_NAME_REGEX, message = Constants.MIDDLE_NAME_INVALID_MSG)
        @Schema(description = "Middle name", example = "Michael")
        private String middleName;

        @Pattern(regexp = Constants.SINGLE_NAME_REGEX, message = Constants.FULL_NAME_INVALID_MSG)
        @Schema(description = "Last name", example = "Doe")
        private String lastName;

        @Pattern(regexp = Constants.SINGLE_NAME_REGEX, message = Constants.NICKNAME_INVALID_MSG)
        @Schema(description = "Nickname", example = "Johnny")
        private String nickname;

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        @Schema(description = "Birth date", example = "1980-05-15")
        private String birthDate;
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

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
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
            @Pattern(regexp = Constants.COUNTRY_CODE_REGEX, message = Constants.COUNTRY_CODE_INVALID_MSG)
            @Schema(description = "Country code", example = "+32")
            private String countryCode;

            @Pattern(regexp = Constants.PHONE_NUMBER_REGEX, message = Constants.PHONE_NUMBER_INVALID_MSG)
            @Schema(description = "Phone number", example = "555-123-4567")
            private String phoneNumber;

            // Email
            @Pattern(regexp = Constants.EMAIL_REGEX, message = Constants.EMAIL_INVALID_MSG)
            @Schema(description = "Email address", example = "john.doe@example.com")
            private String emailAddress;

            // Geographic address
            @Pattern(regexp = Constants.GENERIC_ADDRESS_REGEX, message = Constants.COUNTRY_INVALID_MSG)
            @Schema(description = "Country", example = "United States")
            private String country;

            @Pattern(regexp = Constants.GENERIC_ADDRESS_REGEX, message = Constants.CITY_INVALID_MSG)
            @Schema(description = "City", example = "New York")
            private String city;

            @Pattern(regexp = Constants.GENERIC_ADDRESS_REGEX, message = Constants.STATE_PROVINCE_INVALID_MSG)
            @Schema(description = "State or province", example = "NY")
            private String stateOrProvince;

            @Pattern(regexp = Constants.POSTAL_CODE_REGEX, message = Constants.POSTAL_CODE_INVALID_MSG)
            @Schema(description = "Postal code", example = "10001")
            private String postalCode;

            @Pattern(regexp = Constants.GENERIC_ADDRESS_REGEX, message = Constants.STREET_INVALID_MSG)
            @Schema(description = "Street address line 1", example = "123 Main St")
            private String street1;

            @Pattern(regexp = Constants.GENERIC_ADDRESS_REGEX, message = Constants.STREET_INVALID_MSG)
            @Schema(description = "Street address line 2", example = "Apt 4B")
            private String street2;
        }
    }
}
