package com.tracktainment.duxmanager.api;

import com.tracktainment.duxmanager.domain.Asset;
import com.tracktainment.duxmanager.dto.AssetCreate;
import com.tracktainment.duxmanager.util.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequestMapping("api/v1/assets")
@Validated
@Tag(name = "Assets", description = "Asset management operations")
public interface AssetRestApi {

    @PostMapping(
            path = "/digitalUsers/{digitalUserId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
            summary = "Create a new asset",
            description = "Creates a new asset for a specific digital user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Asset created successfully",
                    content = @Content(schema = @Schema(implementation = Asset.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Authentication failed"),
            @ApiResponse(responseCode = "404", description = "Digital user not found"),
            @ApiResponse(responseCode = "409", description = "Asset already exists")
    })
    ResponseEntity<Asset> create(
            @Parameter(description = "Digital user ID", required = true)
            @PathVariable
            @Pattern(regexp = Constants.ID_REGEX, message = Constants.DIGITAL_USER_ID_INVALID_MSG) String digitalUserId,

            @Parameter(description = "Asset creation data", required = true)
            @RequestBody @Valid AssetCreate assetCreate
    );

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "List assets by criteria",
            description = "Returns a list of assets for a specific digital user filtered by various criteria"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of assets",
                    content = @Content(schema = @Schema(implementation = Asset.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Authentication failed"),
            @ApiResponse(responseCode = "404", description = "Digital user not found")
    })
    ResponseEntity<List<Asset>> listByCriteria(
            @Parameter(description = "Result offset (pagination)")
            @RequestParam(required = false, defaultValue = Constants.DEFAULT_OFFSET)
            @Min(value = Constants.MIN_OFFSET, message = Constants.OFFSET_INVALID_MSG) Integer offset,

            @Parameter(description = "Maximum number of results to return")
            @RequestParam(required = false, defaultValue = Constants.DEFAULT_LIMIT)
            @Min(value = Constants.MIN_LIMIT, message = Constants.LIMIT_INVALID_MSG)
            @Max(value = Constants.MAX_LIMIT, message = Constants.LIMIT_INVALID_MSG) Integer limit,

            @Parameter(description = "Digital user ID", required = true)
            @RequestParam()
            @Pattern(regexp = Constants.ID_REGEX, message = Constants.DIGITAL_USER_ID_INVALID_MSG) String digitalUserId,

            @Parameter(description = "Filter by external IDs (comma-separated)")
            @RequestParam(required = false)
            @Pattern(regexp = Constants.ID_LIST_REGEX, message = Constants.IDS_INVALID_MSG) String externalIds,

            @Parameter(description = "Filter by group ID")
            @RequestParam(required = false)
            @Pattern(regexp = Constants.GROUP_ID_REGEX, message = Constants.GROUP_ID_INVALID_MSG) String groupId,

            @Parameter(description = "Filter by artifact ID")
            @RequestParam(required = false)
            @Pattern(regexp = Constants.ARTIFACT_ID_REGEX, message = Constants.ARTIFACT_ID_INVALID_MSG) String artifactId,

            @Parameter(description = "Filter by asset type")
            @RequestParam(required = false)
            @Pattern(regexp = Constants.TYPE_REGEX, message = Constants.TYPE_INVALID_MSG) String type,

            @Parameter(description = "Filter by creation date")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdAt,

            @Parameter(description = "Filter by date range start")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,

            @Parameter(description = "Filter by date range end")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    );

    @DeleteMapping
    @Operation(
            summary = "Delete an asset",
            description = "Deletes an asset for a specific digital user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Asset deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Authentication failed"),
            @ApiResponse(responseCode = "404", description = "Asset or digital user not found")
    })
    ResponseEntity<Void> delete(
            @Parameter(description = "Digital user ID", required = true)
            @RequestParam
            @Pattern(regexp = Constants.ID_REGEX, message = Constants.DIGITAL_USER_ID_INVALID_MSG) String digitalUserId,

            @Parameter(description = "External ID of the asset to delete", required = true)
            @RequestParam
            @Pattern(regexp = Constants.ID_REGEX, message = Constants.ID_INVALID_MSG) String externalId
    );
}
