package com.tracktainment.duxmanager.usecases.digitaluser;

import com.tracktainment.duxmanager.dataprovider.DigitalUserDataProvider;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteDigitalUserUseCase {

    private final DigitalUserDataProvider digitalUserDataProvider;

    public void execute(Input input) {
        digitalUserDataProvider.delete(input.getDigitalUserId());
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Input {

        private String digitalUserId;
    }
}
