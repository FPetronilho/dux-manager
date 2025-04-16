package com.tracktainment.duxmanager.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tracktainment.duxmanager.domain.Asset;
import com.tracktainment.duxmanager.util.Constants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Data for creating a new asset")
public class AssetCreate {

    @NotNull(message = Constants.ASSET_EXTERNAL_ID_MANDATORY_MSG)
    @Pattern(regexp = Constants.ID_REGEX, message = Constants.EXTERNAL_ID_INVALID_MSG)
    @Schema(description = "External ID from the source system", example = "123e4567-e89b-12d3-a456-426614174000")
    private String externalId;

    @NotNull(message = Constants.ASSET_TYPE_MANDATORY_MSG)
    @Pattern(regexp = Constants.TYPE_REGEX, message = Constants.TYPE_INVALID_MSG)
    @Schema(description = "Type of asset", example = "book")
    private String type;

    @NotNull(message = Constants.ASSET_PERMISSION_POLICY_MANDATORY_MSG)
    @Schema(description = "Permission policy for this asset")
    private Asset.PermissionPolicy permissionPolicy;

    @NotNull(message = Constants.ASSET_ARTIFACT_INFO_MANDATORY_MSG)
    @Schema(description = "Artifact information")
    private Asset.ArtifactInformation artifactInformation;

    @ToString
    @Getter
    @RequiredArgsConstructor
    @Schema(description = "Permission policy types")
    public enum PermissionPolicy {

        @Schema(description = "User owns this asset")
        OWNER("owner"),

        @Schema(description = "User can only view this asset")
        VIEWER("viewer");

        private final String value;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "Information about the artifact")
    public static class ArtifactInformation {

        @NotNull(message = Constants.ASSET_GROUP_ID_MANDATORY_MSG)
        @Pattern(regexp = Constants.GROUP_ID_REGEX, message = Constants.GROUP_ID_INVALID_MSG)
        @Schema(description = "Group ID of the artifact", example = "com.tracktainment")
        private String groupId;

        @NotNull(message = Constants.ASSET_ARTIFACT_ID_MANDATORY_MSG)
        @Pattern(regexp = Constants.ARTIFACT_ID_REGEX, message = Constants.ARTIFACT_ID_INVALID_MSG)
        @Schema(description = "Artifact ID", example = "book-manager")
        private String artifactId;

        @NotNull(message = Constants.ASSET_VERSION_MANDATORY_MSG)
        @Pattern(regexp = Constants.VERSION_REGEX, message = Constants.VERSION_INVALID_MSG)
        @Schema(description = "Version of the artifact", example = "0.0.1-SNAPSHOT")
        private String version;
    }
}