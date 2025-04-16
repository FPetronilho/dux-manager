package com.tracktainment.duxmanager.api;

import com.tracktainment.duxmanager.domain.DigitalUser;
import com.tracktainment.duxmanager.dto.DigitalUserCreate;
import com.tracktainment.duxmanager.util.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/v1/digitalUsers")
@Validated
@Tag(name = "Digital Users", description = "Digital user management operations")
public interface DigitalUserRestApi {

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
            summary = "Create a new digital user",
            description = "Creates a new digital user with the provided details"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Digital user created successfully",
                    content = @Content(schema = @Schema(implementation = DigitalUser.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409", description = "Digital user already exists")
    })
    ResponseEntity<DigitalUser> create(
            @Parameter(description = "Digital user creation data", required = true)
            @RequestBody @Valid DigitalUserCreate digitalUserCreate
    );

    @GetMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
            summary = "Find a digital user by ID",
            description = "Returns a digital user based on the provided ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Digital user found",
                    content = @Content(schema = @Schema(implementation = DigitalUser.class))),
            @ApiResponse(responseCode = "404", description = "Digital user not found")
    })
    ResponseEntity<DigitalUser> findById(
            @Parameter(description = "Digital user ID", required = true)
            @PathVariable @Pattern(regexp = Constants.ID_REGEX, message = Constants.ID_INVALID_MSG) String id
    );

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Find a digital user by subject, identity provider, and tenant",
            description = "Returns a digital user based on the provided subject, identity provider, and tenant ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Digital user found",
                    content = @Content(schema = @Schema(implementation = DigitalUser.class))),
            @ApiResponse(responseCode = "404", description = "Digital user not found")
    })
    ResponseEntity<DigitalUser> findBySubAndIdPAndTenant(
            @Parameter(description = "Subject identifier from identity provider", required = true)
            @RequestParam(name = "identityProviderInformation.subject")
            @Pattern(regexp = Constants.SUB_REGEX, message = Constants.SUB_INVALID_MSG) String subject,

            @Parameter(description = "Identity provider", required = true)
            @RequestParam(name = "identityProviderInformation.identityProvider")
            DigitalUser.IdentityProviderInformation.IdentityProvider identityProvider,

            @Parameter(description = "Tenant identifier", required = true)
            @RequestParam(name = "identityProviderInformation.tenantId")
            @Pattern(regexp = Constants.TENANT_ID_REGEX, message = Constants.TENANT_ID_INVALID_MSG) String tenantId
    );

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a digital user",
            description = "Deletes a digital user based on the provided ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Digital user deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Digital user not found")
    })
    ResponseEntity<Void> delete(
            @Parameter(description = "Digital user ID", required = true)
            @PathVariable @Pattern(regexp = Constants.ID_REGEX, message = Constants.ID_INVALID_MSG) String id
    );
}