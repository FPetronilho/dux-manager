package com.tracktainment.duxmanager.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Asset extends BaseObject {

    private String externalId; // ID from the source system (i.e. book-manager, etc.)
    private String type; // i.e. book, game, etc.
    private PermissionPolicy permissionPolicy;
    private ArtifactInformation artifactInformation;

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

        private String groupId;
        private String artifactId;
        private String version;
    }
}
