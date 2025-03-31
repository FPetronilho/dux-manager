package com.tracktainment.duxmanager.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tracktainment.duxmanager.domain.Asset;
import com.tracktainment.duxmanager.util.Constants;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AssetCreate {

    @NotNull(message = Constants.ASSET_EXTERNAL_ID_MANDATORY_MSG)
    @Pattern(regexp = Constants.ID_REGEX, message = Constants.EXTERNAL_ID_INVALID_MSG)
    private String externalId;

    @NotNull(message = Constants.ASSET_TYPE_MANDATORY_MSG)
    @Pattern(regexp = Constants.TYPE_REGEX, message = Constants.TYPE_INVALID_MSG)
    private String type;

    @NotNull(message = Constants.ASSET_PERMISSION_POLICY_MANDATORY_MSG)
    private Asset.PermissionPolicy permissionPolicy;

    @NotNull(message = Constants.ASSET_ARTIFACT_INFO_MANDATORY_MSG)
    private Asset.ArtifactInformation artifactInformation;

    @ToString
    @Getter
    @RequiredArgsConstructor
    public enum PermissionPolicy {

        OWNER("owner"),
        VIEWER("viewer");

        private final String value;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ArtifactInformation {

        @NotNull(message = Constants.ASSET_GROUP_ID_MANDATORY_MSG)
        @Pattern(regexp = Constants.GROUP_ID_REGEX, message = Constants.GROUP_ID_INVALID_MSG)
        private String groupId;

        @NotNull(message = Constants.ASSET_ARTIFACT_ID_MANDATORY_MSG)
        @Pattern(regexp = Constants.ARTIFACT_ID_REGEX, message = Constants.ARTIFACT_ID_INVALID_MSG)
        private String artifactId;

        @NotNull(message = Constants.ASSET_VERSION_MANDATORY_MSG)
        @Pattern(regexp = Constants.VERSION_REGEX, message = Constants.VERSION_INVALID_MSG)
        private String version;
    }
}
