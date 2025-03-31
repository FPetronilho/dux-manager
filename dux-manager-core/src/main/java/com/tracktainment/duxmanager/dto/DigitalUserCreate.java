package com.tracktainment.duxmanager.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tracktainment.duxmanager.util.Constants;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class DigitalUserCreate {

    private IdentityProviderInformation identityProviderInformation;
    private PersonalInformation personalInformation;
    private List<ContactMedium> contactMediumList;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class IdentityProviderInformation {

        @NotNull(message = Constants.USER_SUBJECT_MANDATORY_MSG)
        @Pattern(regexp = Constants.SUB_REGEX, message = Constants.SUB_INVALID_MSG)
        private String subject;

        private IdentityProviderInformation.IdentityProvider identityProvider;

        @NotNull(message = Constants.USER_TENANT_ID_MANDATORY_MSG)
        @Pattern(regexp = Constants.TENANT_ID_REGEX, message = Constants.TENANT_ID_INVALID_MSG)
        private String tenantId;

        @ToString
        @Getter
        @RequiredArgsConstructor
        public enum IdentityProvider {

            GOOGLE_IDENTITY_PLATFORM("googleIdentityPlatform"),
            APPLE_ID("appleId"),
            MICROSOFT_ENTRA_ID("microsoftEntraId"),
            AMAZON_COGNITO("amazonCognito");

            private final String value;
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private static class PersonalInformation {

        @Pattern(regexp = Constants.FULL_NAME_REGEX, message = Constants.FULL_NAME_INVALID_MSG)
        private String fullName;

        @Pattern(regexp = Constants.SINGLE_NAME_REGEX, message = Constants.FIRST_NAME_INVALID_MSG)
        private String firstName;

        @Pattern(regexp = Constants.SINGLE_NAME_REGEX, message = Constants.MIDDLE_NAME_INVALID_MSG)
        private String middleName;

        @Pattern(regexp = Constants.SINGLE_NAME_REGEX, message = Constants.FULL_NAME_INVALID_MSG)
        private String lastName;

        @Pattern(regexp = Constants.SINGLE_NAME_REGEX, message = Constants.NICKNAME_INVALID_MSG)
        private String nickname;

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        private LocalDate birthDate;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private static class ContactMedium {

        private boolean preferred;
        private ContactMedium.Type type;
        private ContactMedium.Characteristic characteristic;

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
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
        private static class Characteristic {

            // Phone
            @Pattern(regexp = Constants.COUNTRY_CODE_REGEX, message = Constants.COUNTRY_CODE_INVALID_MSG)
            private String countryCode;

            @Pattern(regexp = Constants.PHONE_NUMBER_REGEX, message = Constants.PHONE_NUMBER_INVALID_MSG)
            private String phoneNumber;

            // Email
            @Pattern(regexp = Constants.EMAIL_REGEX, message = Constants.EMAIL_INVALID_MSG)
            private String emailAddress;

            // Geographic address
            @Pattern(regexp = Constants.GENERIC_ADDRESS_REGEX, message = Constants.COUNTRY_INVALID_MSG)
            private String country;

            @Pattern(regexp = Constants.GENERIC_ADDRESS_REGEX, message = Constants.CITY_INVALID_MSG)
            private String city;

            @Pattern(regexp = Constants.GENERIC_ADDRESS_REGEX, message = Constants.STATE_PROVINCE_INVALID_MSG)
            private String stateOrProvince;

            @Pattern(regexp = Constants.POSTAL_CODE_REGEX, message = Constants.POSTAL_CODE_INVALID_MSG)
            private String postalCode;

            @Pattern(regexp = Constants.GENERIC_ADDRESS_REGEX, message = Constants.STREET_INVALID_MSG)
            private String street1;

            @Pattern(regexp = Constants.GENERIC_ADDRESS_REGEX, message = Constants.STREET_INVALID_MSG)
            private String street2;
        }
    }
}
