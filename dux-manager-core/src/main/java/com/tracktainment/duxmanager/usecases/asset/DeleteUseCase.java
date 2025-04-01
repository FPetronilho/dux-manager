package com.tracktainment.duxmanager.usecases.asset;

import com.tracktainment.duxmanager.dataprovider.DuxDataProvider;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteUseCase {

    private final DuxDataProvider duxDataProvider;

    public void execute(Input input) {
        duxDataProvider.deleteAsset(
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
