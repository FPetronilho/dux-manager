package com.tracktainment.duxmanager.usecases.asset;

import com.tracktainment.duxmanager.dataprovider.DuxDataProvider;
import com.tracktainment.duxmanager.domain.Asset;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FindByExternalIdUseCase {

    private final DuxDataProvider duxDataProvider;

    public Output execute(Input input) {
        return Output.builder()
                .asset(duxDataProvider.findAssetByExternalId(
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
