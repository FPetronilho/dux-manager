package dto;

import com.tracktainment.duxmanager.dto.AssetCreate;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AssetCreateTest {

    @Test
    void shouldCreateAssetCreateUsingBuilder() {
        // Arrange
        String externalId = UUID.randomUUID().toString();
        String type = "book";
        AssetCreate.PermissionPolicy permissionPolicy = AssetCreate.PermissionPolicy.OWNER;

        AssetCreate.ArtifactInformation artifactInfo = AssetCreate.ArtifactInformation.builder()
                .groupId("com.tracktainment")
                .artifactId("test-artifact")
                .version("1.0.0")
                .build();

        // Act
        AssetCreate assetCreate = AssetCreate.builder()
                .externalId(externalId)
                .type(type)
                .permissionPolicy(permissionPolicy)
                .artifactInformation(artifactInfo)
                .build();

        // Assert
        assertEquals(externalId, assetCreate.getExternalId());
        assertEquals(type, assetCreate.getType());
        assertEquals(permissionPolicy, assetCreate.getPermissionPolicy());
        assertEquals(artifactInfo, assetCreate.getArtifactInformation());
    }

    @Test
    void shouldCreateEmptyAssetCreateUsingNoArgsConstructor() {
        // Act
        AssetCreate assetCreate = new AssetCreate();

        // Assert
        assertNull(assetCreate.getExternalId());
        assertNull(assetCreate.getType());
        assertNull(assetCreate.getPermissionPolicy());
        assertNull(assetCreate.getArtifactInformation());
    }

    @Test
    void shouldUseSettersAndGetters() {
        // Arrange
        AssetCreate assetCreate = new AssetCreate();
        String externalId = UUID.randomUUID().toString();
        String type = "movie";
        AssetCreate.PermissionPolicy permissionPolicy = AssetCreate.PermissionPolicy.VIEWER;

        AssetCreate.ArtifactInformation artifactInfo = AssetCreate.ArtifactInformation.builder()
                .groupId("com.tracktainment")
                .artifactId("test-artifact")
                .version("1.0.0")
                .build();

        // Act
        assetCreate.setExternalId(externalId);
        assetCreate.setType(type);
        assetCreate.setPermissionPolicy(permissionPolicy);
        assetCreate.setArtifactInformation(artifactInfo);

        // Assert
        assertEquals(externalId, assetCreate.getExternalId());
        assertEquals(type, assetCreate.getType());
        assertEquals(permissionPolicy, assetCreate.getPermissionPolicy());
        assertEquals(artifactInfo, assetCreate.getArtifactInformation());
    }

    @Test
    void shouldImplementToString() {
        // Arrange
        AssetCreate assetCreate = AssetCreate.builder()
                .externalId(UUID.randomUUID().toString())
                .type("book")
                .build();

        // Act
        String toString = assetCreate.toString();

        // Assert
        assertNotNull(toString);
        assertFalse(toString.isEmpty());
        assertTrue(toString.contains("AssetCreate"));
    }

    @Test
    void shouldCreateArtifactInformationUsingBuilder() {
        // Arrange
        String groupId = "com.tracktainment";
        String artifactId = "test-artifact";
        String version = "1.0.0";

        // Act
        AssetCreate.ArtifactInformation artifactInfo = AssetCreate.ArtifactInformation.builder()
                .groupId(groupId)
                .artifactId(artifactId)
                .version(version)
                .build();

        // Assert
        assertEquals(groupId, artifactInfo.getGroupId());
        assertEquals(artifactId, artifactInfo.getArtifactId());
        assertEquals(version, artifactInfo.getVersion());
    }

    @Test
    void shouldCreateEmptyArtifactInformationUsingNoArgsConstructor() {
        // Act
        AssetCreate.ArtifactInformation artifactInfo = new AssetCreate.ArtifactInformation();

        // Assert
        assertNull(artifactInfo.getGroupId());
        assertNull(artifactInfo.getArtifactId());
        assertNull(artifactInfo.getVersion());
    }

    @Test
    void permissionPolicyShouldHaveCorrectValues() {
        // Assert
        assertEquals("owner", AssetCreate.PermissionPolicy.OWNER.getValue());
        assertEquals("viewer", AssetCreate.PermissionPolicy.VIEWER.getValue());
        assertEquals(2, AssetCreate.PermissionPolicy.values().length);
    }

    @Test
    void permissionPolicyShouldHaveCorrectToString() {
        // Assert
        assertTrue(AssetCreate.PermissionPolicy.OWNER.toString().contains("OWNER"));
        assertTrue(AssetCreate.PermissionPolicy.VIEWER.toString().contains("VIEWER"));
    }
}
