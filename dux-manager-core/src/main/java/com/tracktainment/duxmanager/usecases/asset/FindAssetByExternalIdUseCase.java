package com.tracktainment.duxmanager.usecases.asset;

import com.tracktainment.duxmanager.dataprovider.AssetDataProvider;
import com.tracktainment.duxmanager.domain.Asset;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FindAssetByExternalIdUseCase {

    private final AssetDataProvider assetDataProvider;

    public Output execute(Input input) {
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
