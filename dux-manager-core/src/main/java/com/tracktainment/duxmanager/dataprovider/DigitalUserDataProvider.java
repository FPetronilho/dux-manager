package com.tracktainment.duxmanager.dataprovider;

import com.tracktainment.duxmanager.domain.DigitalUser;
import com.tracktainment.duxmanager.dto.DigitalUserCreate;

public interface DigitalUserDataProvider {

    DigitalUser create(DigitalUserCreate digitalUserCreate);

    DigitalUser findById(String id);

    DigitalUser findBySubAndIdPAndTenant(
            String subject,
            DigitalUser.IdentityProviderInformation.IdentityProvider identityProvider,
            String tenantId
    );

    void delete(String id);
}
