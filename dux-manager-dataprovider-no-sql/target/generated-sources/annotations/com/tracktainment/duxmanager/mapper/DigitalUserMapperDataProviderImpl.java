package com.tracktainment.duxmanager.mapper;

import com.tracktainment.duxmanager.document.DigitalUserDocument;
import com.tracktainment.duxmanager.domain.Asset;
import com.tracktainment.duxmanager.domain.DigitalUser;
import com.tracktainment.duxmanager.dto.DigitalUserCreate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-03T18:15:41+0200",
    comments = "version: 1.6.2, compiler: javac, environment: Java 23 (Oracle Corporation)"
)
@Component
public class DigitalUserMapperDataProviderImpl implements DigitalUserMapperDataProvider {

    @Override
    public DigitalUser toDigitalUser(DigitalUserDocument digitalUserDocument) {
        if ( digitalUserDocument == null ) {
            return null;
        }

        DigitalUser.DigitalUserBuilder<?, ?> digitalUser = DigitalUser.builder();

        digitalUser.id( digitalUserDocument.getId() );
        digitalUser.createdAt( digitalUserDocument.getCreatedAt() );
        digitalUser.updatedAt( digitalUserDocument.getUpdatedAt() );
        digitalUser.identityProviderInformation( digitalUserDocument.getIdentityProviderInformation() );
        digitalUser.personalInformation( digitalUserDocument.getPersonalInformation() );
        List<DigitalUser.ContactMedium> list = digitalUserDocument.getContactMediumList();
        if ( list != null ) {
            digitalUser.contactMediumList( new ArrayList<DigitalUser.ContactMedium>( list ) );
        }
        List<Asset> list1 = digitalUserDocument.getAssets();
        if ( list1 != null ) {
            digitalUser.assets( new ArrayList<Asset>( list1 ) );
        }

        return digitalUser.build();
    }

    @Override
    public DigitalUserDocument toDigitalUserDocument(DigitalUserCreate digitalUserCreate) {
        if ( digitalUserCreate == null ) {
            return null;
        }

        DigitalUserDocument.DigitalUserDocumentBuilder<?, ?> digitalUserDocument = DigitalUserDocument.builder();

        digitalUserDocument.identityProviderInformation( identityProviderInformationToIdentityProviderInformation( digitalUserCreate.getIdentityProviderInformation() ) );
        digitalUserDocument.personalInformation( personalInformationToPersonalInformation( digitalUserCreate.getPersonalInformation() ) );
        digitalUserDocument.contactMediumList( contactMediumListToContactMediumList( digitalUserCreate.getContactMediumList() ) );

        digitalUserDocument.id( java.util.UUID.randomUUID().toString() );

        return digitalUserDocument.build();
    }

    protected DigitalUser.IdentityProviderInformation.IdentityProvider identityProviderToIdentityProvider(DigitalUserCreate.IdentityProviderInformation.IdentityProvider identityProvider) {
        if ( identityProvider == null ) {
            return null;
        }

        DigitalUser.IdentityProviderInformation.IdentityProvider identityProvider1;

        switch ( identityProvider ) {
            case GOOGLE_IDENTITY_PLATFORM: identityProvider1 = DigitalUser.IdentityProviderInformation.IdentityProvider.GOOGLE_IDENTITY_PLATFORM;
            break;
            case APPLE_ID: identityProvider1 = DigitalUser.IdentityProviderInformation.IdentityProvider.APPLE_ID;
            break;
            case MICROSOFT_ENTRA_ID: identityProvider1 = DigitalUser.IdentityProviderInformation.IdentityProvider.MICROSOFT_ENTRA_ID;
            break;
            case AMAZON_COGNITO: identityProvider1 = DigitalUser.IdentityProviderInformation.IdentityProvider.AMAZON_COGNITO;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + identityProvider );
        }

        return identityProvider1;
    }

    protected DigitalUser.IdentityProviderInformation identityProviderInformationToIdentityProviderInformation(DigitalUserCreate.IdentityProviderInformation identityProviderInformation) {
        if ( identityProviderInformation == null ) {
            return null;
        }

        DigitalUser.IdentityProviderInformation.IdentityProviderInformationBuilder identityProviderInformation1 = DigitalUser.IdentityProviderInformation.builder();

        identityProviderInformation1.subject( identityProviderInformation.getSubject() );
        identityProviderInformation1.identityProvider( identityProviderToIdentityProvider( identityProviderInformation.getIdentityProvider() ) );
        identityProviderInformation1.tenantId( identityProviderInformation.getTenantId() );

        return identityProviderInformation1.build();
    }

    protected DigitalUser.PersonalInformation personalInformationToPersonalInformation(DigitalUserCreate.PersonalInformation personalInformation) {
        if ( personalInformation == null ) {
            return null;
        }

        DigitalUser.PersonalInformation.PersonalInformationBuilder personalInformation1 = DigitalUser.PersonalInformation.builder();

        personalInformation1.fullName( personalInformation.getFullName() );
        personalInformation1.firstName( personalInformation.getFirstName() );
        personalInformation1.middleName( personalInformation.getMiddleName() );
        personalInformation1.lastName( personalInformation.getLastName() );
        personalInformation1.nickname( personalInformation.getNickname() );
        personalInformation1.birthDate( personalInformation.getBirthDate() );

        return personalInformation1.build();
    }

    protected DigitalUser.ContactMedium.Type typeToType(DigitalUserCreate.ContactMedium.Type type) {
        if ( type == null ) {
            return null;
        }

        DigitalUser.ContactMedium.Type type1;

        switch ( type ) {
            case PHONE: type1 = DigitalUser.ContactMedium.Type.PHONE;
            break;
            case EMAIL: type1 = DigitalUser.ContactMedium.Type.EMAIL;
            break;
            case GEOGRAPHIC_ADDRESS: type1 = DigitalUser.ContactMedium.Type.GEOGRAPHIC_ADDRESS;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + type );
        }

        return type1;
    }

    protected DigitalUser.ContactMedium.Characteristic characteristicToCharacteristic(DigitalUserCreate.ContactMedium.Characteristic characteristic) {
        if ( characteristic == null ) {
            return null;
        }

        DigitalUser.ContactMedium.Characteristic.CharacteristicBuilder characteristic1 = DigitalUser.ContactMedium.Characteristic.builder();

        characteristic1.countryCode( characteristic.getCountryCode() );
        characteristic1.phoneNumber( characteristic.getPhoneNumber() );
        characteristic1.emailAddress( characteristic.getEmailAddress() );
        characteristic1.country( characteristic.getCountry() );
        characteristic1.city( characteristic.getCity() );
        characteristic1.stateOrProvince( characteristic.getStateOrProvince() );
        characteristic1.postalCode( characteristic.getPostalCode() );
        characteristic1.street1( characteristic.getStreet1() );
        characteristic1.street2( characteristic.getStreet2() );

        return characteristic1.build();
    }

    protected DigitalUser.ContactMedium contactMediumToContactMedium(DigitalUserCreate.ContactMedium contactMedium) {
        if ( contactMedium == null ) {
            return null;
        }

        DigitalUser.ContactMedium.ContactMediumBuilder contactMedium1 = DigitalUser.ContactMedium.builder();

        contactMedium1.preferred( contactMedium.isPreferred() );
        contactMedium1.type( typeToType( contactMedium.getType() ) );
        contactMedium1.characteristic( characteristicToCharacteristic( contactMedium.getCharacteristic() ) );
        contactMedium1.expiresAt( contactMedium.getExpiresAt() );

        return contactMedium1.build();
    }

    protected List<DigitalUser.ContactMedium> contactMediumListToContactMediumList(List<DigitalUserCreate.ContactMedium> list) {
        if ( list == null ) {
            return null;
        }

        List<DigitalUser.ContactMedium> list1 = new ArrayList<DigitalUser.ContactMedium>( list.size() );
        for ( DigitalUserCreate.ContactMedium contactMedium : list ) {
            list1.add( contactMediumToContactMedium( contactMedium ) );
        }

        return list1;
    }
}
