package com.tracktainment.duxmanager.usecases.digitaluser;

import com.tracktainment.duxmanager.dataprovider.DuxDataProvider;
import com.tracktainment.duxmanager.domain.DigitalUser;
import com.tracktainment.duxmanager.dto.DigitalUserCreate;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateUseCase {

    private final DuxDataProvider duxDataProvider;

    public Output execute(Input input) {
        return Output.builder()
                .digitalUser(duxDataProvider.createDigitalUser(input.getDigitalUserCreate()))
                .build();
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Input {

        private DigitalUserCreate digitalUserCreate;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Output {

        private DigitalUser digitalUser;
    }
}
