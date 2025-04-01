package com.tracktainment.duxmanager.document;

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

    private DigitalUser.IdentityProviderInformation identityProviderInformation;
    private DigitalUser.PersonalInformation personalInformation;
    private List<DigitalUser.ContactMedium> contactMediumList;

    @Field("assets")
    private List<Asset> assets = new ArrayList<>();
}
