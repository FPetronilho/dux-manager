package testutil;

import com.tracktainment.duxmanager.domain.Asset;
import com.tracktainment.duxmanager.dto.AssetCreate;
import com.tracktainment.duxmanager.security.DigitalUserSecurityContext;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TestAssetDataUtil {

    public static AssetCreate createTestAssetCreate1() {
        return AssetCreate.builder()
                .externalId("123e4567-e89b-12d3-a456-426614174001")
                .type("book")
                .permissionPolicy(AssetCreate.PermissionPolicy.OWNER)
                .artifactInformation(AssetCreate.ArtifactInformation.builder()
                        .groupId("com.tracktainment")
                        .artifactId("book-manager")
                        .version("1.0.0")
                        .build())
                .build();
    }

    public static AssetCreate createTestAssetCreate2() {
        return AssetCreate.builder()
                .externalId("123e4567-e89b-12d3-a456-426614174005")
                .type("game")
                .permissionPolicy(AssetCreate.PermissionPolicy.VIEWER)
                .artifactInformation(AssetCreate.ArtifactInformation.builder()
                        .groupId("com.tracktainment")
                        .artifactId("game-manager")
                        .version("1.0.0")
                        .build())
                .build();
    }

    public static Asset createTestAsset1() {
        return Asset.builder()
                .id("123e4567-e89b-12d3-a456-426614174002")
                .externalId(createTestAssetCreate1().getExternalId())
                .type(createTestAssetCreate1().getType())
                .permissionPolicy(Asset.PermissionPolicy.OWNER)
                .artifactInformation(Asset.ArtifactInformation.builder()
                        .groupId(createTestAssetCreate1().getArtifactInformation().getGroupId())
                        .artifactId(createTestAssetCreate1().getArtifactInformation().getArtifactId())
                        .version(createTestAssetCreate1().getArtifactInformation().getVersion())
                        .build())
                .createdAt(LocalDate.now().atTime(12, 0))
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static Asset createTestAsset2() {
        return Asset.builder()
                .id("123e4567-e89b-12d3-a456-426614174004")
                .externalId(createTestAssetCreate2().getExternalId())
                .type(createTestAssetCreate2().getType())
                .permissionPolicy(Asset.PermissionPolicy.VIEWER)
                .artifactInformation(Asset.ArtifactInformation.builder()
                        .groupId(createTestAssetCreate2().getArtifactInformation().getGroupId())
                        .artifactId(createTestAssetCreate2().getArtifactInformation().getArtifactId())
                        .version(createTestAssetCreate2().getArtifactInformation().getVersion())
                        .build())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static DigitalUserSecurityContext createTestDigitalUserSecurityContext() {
        return DigitalUserSecurityContext.builder()
                .id("123e4567-e89b-12d3-a456-426614174000")
                .subject("auth2|123456789")
                .build();
    }
}
