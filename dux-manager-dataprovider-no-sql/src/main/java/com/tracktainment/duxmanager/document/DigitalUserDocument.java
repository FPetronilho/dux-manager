package com.tracktainment.duxmanager.document;

import com.tracktainment.duxmanager.annotation.Encrypted;
import com.tracktainment.duxmanager.domain.Asset;
import com.tracktainment.duxmanager.domain.DigitalUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Document(collection = "digital-users")
public class DigitalUserDocument extends BaseDocument {

    @Indexed(unique = true)
    private String id;

    private IdentityProviderInformation identityProviderInformation;
    private PersonalInformation personalInformation;
    private List<ContactMedium> contactMediumList;

    @Field("assets")
    private List<Asset> assets = new ArrayList<>();

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @EqualsAndHashCode
    public static class IdentityProviderInformation {
        private String subject;
        private DigitalUser.IdentityProviderInformation.IdentityProvider identityProvider;
        private String tenantId;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @EqualsAndHashCode
    public static class PersonalInformation {

        @Encrypted
        private String fullName;

        @Encrypted
        private String firstName;

        @Encrypted
        private String middleName;

        @Encrypted
        private String lastName;

        @Encrypted
        private String nickname;

        @Encrypted
        private String birthDate;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @EqualsAndHashCode
    public static class ContactMedium {

        private boolean preferred;
        private DigitalUser.ContactMedium.Type type;
        private Characteristic characteristic;
        private LocalDateTime expiresAt;

        @AllArgsConstructor
        @NoArgsConstructor
        @Data
        @EqualsAndHashCode
        public static class Characteristic {

            // Phone
            @Encrypted
            private String countryCode;

            @Encrypted
            private String phoneNumber;

            // Email
            @Encrypted
            private String emailAddress;

            // Geographic address
            @Encrypted
            private String country;

            @Encrypted
            private String city;

            @Encrypted
            private String stateOrProvince;

            @Encrypted
            private String postalCode;

            @Encrypted
            private String street1;

            @Encrypted
            private String street2;
        }
    }
}
