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
public class DeleteUseCase {

    private final DuxDataProvider duxDataProvider;

    public void execute(Input input) {
        duxDataProvider.deleteDigitalUser(input.getDigitalUserId());
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Input {

        private String digitalUserId;
    }
}
