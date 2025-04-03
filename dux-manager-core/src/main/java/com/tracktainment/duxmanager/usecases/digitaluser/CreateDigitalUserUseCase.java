package com.tracktainment.duxmanager.usecases.digitaluser;

import com.tracktainment.duxmanager.dataprovider.DigitalUserDataProvider;
import com.tracktainment.duxmanager.domain.DigitalUser;
import com.tracktainment.duxmanager.dto.DigitalUserCreate;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateDigitalUserUseCase {

    private final DigitalUserDataProvider digitalUserDataProvider;

    public Output execute(Input input) {
        return Output.builder()
                .digitalUser(digitalUserDataProvider.create(input.getDigitalUserCreate()))
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
