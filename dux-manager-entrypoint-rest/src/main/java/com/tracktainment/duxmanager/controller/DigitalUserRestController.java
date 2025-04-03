package com.tracktainment.duxmanager.controller;

import com.tracktainment.duxmanager.api.DigitalUserRestApi;
import com.tracktainment.duxmanager.domain.DigitalUser;
import com.tracktainment.duxmanager.dto.DigitalUserCreate;
import com.tracktainment.duxmanager.usecases.digitaluser.CreateDigitalUserUseCase;
import com.tracktainment.duxmanager.usecases.digitaluser.DeleteDigitalUserUseCase;
import com.tracktainment.duxmanager.usecases.digitaluser.FindDigitalUserByIdUseCase;
import com.tracktainment.duxmanager.usecases.digitaluser.FindDigitalUserBySubAndIdPAndTenantUseCase;
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

    private final CreateDigitalUserUseCase createDigitalUserUseCase;
    private final FindDigitalUserByIdUseCase findDigitalUserByIdUseCase;
    private final FindDigitalUserBySubAndIdPAndTenantUseCase findDigitalUserBySubAndIdPAndTenantUseCase;
    private final DeleteDigitalUserUseCase deleteDigitalUserUseCase;

    @Override
    public ResponseEntity<DigitalUser> create(DigitalUserCreate digitalUserCreate) {
        log.info("Creating digital user: {}", digitalUserCreate);
        CreateDigitalUserUseCase.Input input = CreateDigitalUserUseCase.Input.builder()
                .digitalUserCreate(digitalUserCreate)
                .build();

        CreateDigitalUserUseCase.Output output = createDigitalUserUseCase.execute(input);
        return new ResponseEntity<>(output.getDigitalUser(), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<DigitalUser> findById(String id) {
        log.info("Finding digital user by id: {}", id);
        FindDigitalUserByIdUseCase.Input input = FindDigitalUserByIdUseCase.Input.builder()
                .digitalUserId(id)
                .build();

        FindDigitalUserByIdUseCase.Output output = findDigitalUserByIdUseCase.execute(input);
        return new ResponseEntity<>(output.getDigitalUser(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<DigitalUser> findBySubAndIdPAndTenant(
            String subject,
            DigitalUser.IdentityProviderInformation.IdentityProvider identityProvider,
            String tenantId
    ) {
        log.info(
                "Finding digital user by subject: {}, identity provider: {} and tenant ID: {}",
                subject,
                identityProvider,
                tenantId
        );

        FindDigitalUserBySubAndIdPAndTenantUseCase.Input input =
                FindDigitalUserBySubAndIdPAndTenantUseCase.Input.builder()
                .subject(subject)
                .identityProvider(identityProvider)
                .tenantId(tenantId)
                .build();

        FindDigitalUserBySubAndIdPAndTenantUseCase.Output output =
                findDigitalUserBySubAndIdPAndTenantUseCase.execute(input);

        return new ResponseEntity<>(output.getDigitalUser(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> delete(String id) {
        log.info("Deleting digital user by id: {}", id);
        DeleteDigitalUserUseCase.Input input = DeleteDigitalUserUseCase.Input.builder()
                .digitalUserId(id)
                .build();

        deleteDigitalUserUseCase.execute(input);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
