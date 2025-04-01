package com.tracktainment.duxmanager.controller;

import com.tracktainment.duxmanager.api.DigitalUserRestApi;
import com.tracktainment.duxmanager.domain.DigitalUser;
import com.tracktainment.duxmanager.dto.DigitalUserCreate;
import com.tracktainment.duxmanager.usecases.digitaluser.CreateUseCase;
import com.tracktainment.duxmanager.usecases.digitaluser.DeleteUseCase;
import com.tracktainment.duxmanager.usecases.digitaluser.FindByIdUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
public class DigitalUserRestController implements DigitalUserRestApi {

    private final CreateUseCase createUseCase;
    private final FindByIdUseCase findByIdUseCase;
    private final DeleteUseCase deleteUseCase;

    @Override
    public ResponseEntity<DigitalUser> create(DigitalUserCreate digitalUserCreate) {
        log.info("Creating digital user: {}", digitalUserCreate);
        CreateUseCase.Input input = CreateUseCase.Input.builder()
                .digitalUserCreate(digitalUserCreate)
                .build();

        CreateUseCase.Output output = createUseCase.execute(input);
        return new ResponseEntity<>(output.getDigitalUser(), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<DigitalUser> findById(String id) {
        log.info("Finding digital user by id: {}", id);
        FindByIdUseCase.Input input = FindByIdUseCase.Input.builder()
                .digitalUserId(id)
                .build();

        FindByIdUseCase.Output output = findByIdUseCase.execute(input);
        return new ResponseEntity<>(output.getDigitalUser(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> delete(String id) {
        log.info("Deleting digital user by id: {}", id);
        DeleteUseCase.Input input = DeleteUseCase.Input.builder()
                .digitalUserId(id)
                .build();

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
