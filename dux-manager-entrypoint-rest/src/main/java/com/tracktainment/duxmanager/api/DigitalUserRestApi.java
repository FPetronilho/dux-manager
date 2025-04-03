package com.tracktainment.duxmanager.api;

import com.tracktainment.duxmanager.domain.DigitalUser;
import com.tracktainment.duxmanager.dto.DigitalUserCreate;
import com.tracktainment.duxmanager.util.Constants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/v1/digitalUsers")
@Validated
public interface DigitalUserRestApi {

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<DigitalUser> create(@RequestBody @Valid  DigitalUserCreate digitalUserCreate);

    @GetMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<DigitalUser> findById(
            @PathVariable @Pattern(regexp = Constants.ID_REGEX, message = Constants.ID_INVALID_MSG) String id
    );

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<DigitalUser> findBySubAndIdPAndTenant(
            @RequestParam(name = "identityProviderInformation.subject")
            @Pattern(regexp = Constants.SUB_REGEX, message = Constants.SUB_INVALID_MSG) String subject,

            @RequestParam(name = "identityProviderInformation.identityProvider")
            DigitalUser.IdentityProviderInformation.IdentityProvider identityProvider,

            @RequestParam(name = "identityProviderInformation.tenantId")
            @Pattern(regexp = Constants.TENANT_ID_REGEX, message = Constants.TENANT_ID_INVALID_MSG) String tenantId
    );

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(
            @PathVariable @Pattern(regexp = Constants.ID_REGEX, message = Constants.ID_INVALID_MSG) String id
    );
}
