package util;

import com.tracktainment.duxmanager.domain.Asset;
import com.tracktainment.duxmanager.domain.DigitalUser;
import com.tracktainment.duxmanager.dto.AssetCreate;
import com.tracktainment.duxmanager.security.context.DigitalUserSecurityContext;

public class TestDataUtil {

    public static AssetCreate createTestAssetCreate() {
        return AssetCreate.builder()
                .externalId("123e4567-e89b-12d3-a456-426614174001")
                .type("book")
                .permissionPolicy(Asset.PermissionPolicy.OWNER)
                .artifactInformation(Asset.ArtifactInformation.builder()
                        .groupId("com.tracktainment")
                        .artifactId("book-manager")
                        .version("1.0.0")
                        .build())
                .build();
    }

    public static Asset createTestAsset() {
        return Asset.builder()
                .id("123e4567-e89b-12d3-a456-426614174002")
                .externalId(createTestAssetCreate().getExternalId())
                .type(createTestAssetCreate().getType())
                .permissionPolicy(createTestAssetCreate().getPermissionPolicy())
                .artifactInformation(Asset.ArtifactInformation.builder()
                        .groupId(createTestAssetCreate().getArtifactInformation().getGroupId())
                        .artifactId(createTestAssetCreate().getArtifactInformation().getArtifactId())
                        .version(createTestAssetCreate().getArtifactInformation().getVersion())
                        .build())
                .build();
    }

    public static DigitalUserSecurityContext createTestDigitalUserSecurityContext() {
        return DigitalUserSecurityContext.builder()
                .id("123e4567-e89b-12d3-a456-426614174000")
                .subject("auth2|123456789")
                .build();
    }
}
