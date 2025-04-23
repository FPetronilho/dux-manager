package com.tracktainment.duxmanager.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Asset information")
public class Asset extends BaseObject {

    @Schema(description = "External ID from the source system", example = "123e4567-e89b-12d3-a456-426614174000")
    private String externalId;

    @Schema(description = "Type of asset", example = "book")
    private String type;

    @Schema(description = "Permission policy for this asset")
    private PermissionPolicy permissionPolicy;

    @Schema(description = "Artifact information")
    private ArtifactInformation artifactInformation;

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

        @Schema(description = "Group ID of the artifact", example = "com.tracktainment")
        private String groupId;

        @Schema(description = "Artifact ID", example = "book-manager")
        private String artifactId;

        @Schema(description = "Version of the artifact", example = "0.0.1-SNAPSHOT")
        private String version;
    }
}
