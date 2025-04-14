package com.tracktainment.duxmanager.usecases.asset;

import com.tracktainment.duxmanager.dataprovider.AssetDataProvider;
import com.tracktainment.duxmanager.domain.Asset;
import com.tracktainment.duxmanager.exception.AuthenticationFailedException;
import com.tracktainment.duxmanager.security.context.DigitalUserSecurityContext;
import com.tracktainment.duxmanager.security.util.SecurityUtil;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FindAssetByExternalIdUseCase {

    private final AssetDataProvider assetDataProvider;
    private final SecurityUtil securityUtil;

    public Output execute(Input input) {
        // Check if digital user is authenticated
        DigitalUserSecurityContext digitalUserSecurityContext = securityUtil.getDigitalUser();
        if (!digitalUserSecurityContext.getId().equals(input.getDigitalUserId())) {
            throw new AuthenticationFailedException("Authentication Failed: User ID does not match ID from JWT.");
        }

        return Output.builder()
                .asset(assetDataProvider.findByExternalId(
                        input.getDigitalUserId(),
                        input.getExternalId()
                ))
                .build();
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Input {

        private String digitalUserId;
        private String externalId;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Output {

        private Asset asset;
    }
}
