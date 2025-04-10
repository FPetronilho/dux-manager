package com.tracktainment.duxmanager.usecases.asset;

import com.tracktainment.duxmanager.dataprovider.AssetDataProvider;
import com.tracktainment.duxmanager.domain.Asset;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ListAssetsByCriteriaUseCase {

    private final AssetDataProvider assetDataProvider;

    public Output execute(Input input) {
        return Output.builder()
                .assets(assetDataProvider.listByCriteria(input))
                .build();
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Input {

        private Integer offset;
        private Integer limit;
        private String digitalUserId;
        private String externalIds;
        private String groupId;
        private String artifactId;
        private String type;
        private LocalDate createdAt;
        private LocalDate from;
        private LocalDate to;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Output {

        private List<Asset> assets;
    }
}
