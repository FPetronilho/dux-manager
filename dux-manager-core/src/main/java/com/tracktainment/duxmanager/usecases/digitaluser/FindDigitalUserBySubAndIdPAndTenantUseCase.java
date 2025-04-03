package com.tracktainment.duxmanager.usecases.digitaluser;

import com.tracktainment.duxmanager.dataprovider.DigitalUserDataProvider;
import com.tracktainment.duxmanager.domain.DigitalUser;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FindDigitalUserBySubAndIdPAndTenantUseCase {

    private final DigitalUserDataProvider digitalUserDataProvider;

    public Output execute(Input input) {
        return Output.builder()
                .digitalUser(digitalUserDataProvider.findBySubAndIdPAndTenant(
                        input.getSubject(),
                        input.getIdentityProvider(),
                        input.getTenantId()
                ))
                .build();
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Input {

        private String subject;
        private DigitalUser.IdentityProviderInformation.IdentityProvider identityProvider;
        private String tenantId;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Output {

        private DigitalUser digitalUser;
    }
}
