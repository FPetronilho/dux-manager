package com.tracktainment.duxmanager.usecases.asset;

import com.tracktainment.duxmanager.dataprovider.AssetDataProvider;
import com.tracktainment.duxmanager.exception.AuthenticationFailedException;
import com.tracktainment.duxmanager.security.DigitalUserSecurityContext;
import com.tracktainment.duxmanager.security.SecurityUtil;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteAssetUseCase {

    private final AssetDataProvider assetDataProvider;
    private final SecurityUtil securityUtil;

    public void execute(Input input) {
        // Check if digital user is authenticated
        DigitalUserSecurityContext digitalUserSecurityContext = securityUtil.getDigitalUser();
        if (!digitalUserSecurityContext.getId().equals(input.getDigitalUserId())) {
            throw new AuthenticationFailedException("Authentication Failed: User ID does not match ID from JWT.");
        }
        assetDataProvider.delete(
                input.getDigitalUserId(),
                input.getExternalId()
        );
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Input {

        private String digitalUserId;
        private String externalId;
    }
}
