package domain;

import com.tracktainment.duxmanager.domain.Asset;
import com.tracktainment.duxmanager.domain.BaseObject;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AssetTest {

    @Test
    void shouldCreateAssetUsingBuilder() {
        // Arrange
        String id = UUID.randomUUID().toString();
        String externalId = UUID.randomUUID().toString();
        String type = "book";
        Asset.PermissionPolicy permissionPolicy = Asset.PermissionPolicy.OWNER;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now().plusDays(1);

        Asset.ArtifactInformation artifactInfo = Asset.ArtifactInformation.builder()
                .groupId("com.tracktainment")
                .artifactId("test-artifact")
                .version("1.0.0")
                .build();

        // Act
        Asset asset = Asset.builder()
                .id(id)
                .externalId(externalId)
                .type(type)
                .permissionPolicy(permissionPolicy)
                .artifactInformation(artifactInfo)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        // Assert
        assertEquals(id, asset.getId());
        assertEquals(externalId, asset.getExternalId());
        assertEquals(type, asset.getType());
        assertEquals(permissionPolicy, asset.getPermissionPolicy());
        assertEquals(artifactInfo, asset.getArtifactInformation());
        assertEquals(createdAt, asset.getCreatedAt());
        assertEquals(updatedAt, asset.getUpdatedAt());
    }

    @Test
    void shouldCreateEmptyAssetUsingNoArgsConstructor() {
        // Act
        Asset asset = new Asset();

        // Assert
        assertNull(asset.getId());
        assertNull(asset.getExternalId());
        assertNull(asset.getType());
        assertNull(asset.getPermissionPolicy());
        assertNull(asset.getArtifactInformation());
        assertNull(asset.getCreatedAt());
        assertNull(asset.getUpdatedAt());
    }

    @Test
    void shouldInheritFromBaseObject() {
        // Arrange
        Asset asset = new Asset();

        // Assert
        assertTrue(asset instanceof BaseObject);
    }

    @Test
    void shouldImplementEqualsAndHashCodeCorrectly() {
        // Arrange
        String id1 = UUID.randomUUID().toString();
        String id2 = UUID.randomUUID().toString();

        Asset asset1 = Asset.builder().id(id1).build();
        Asset asset2 = Asset.builder().id(id1).build(); // Same ID
        Asset asset3 = Asset.builder().id(id2).build(); // Different ID

        // Assert
        assertEquals(asset1, asset2); // Same ID should be equal
        assertNotEquals(asset1, asset3); // Different ID should not be equal
        assertEquals(asset1.hashCode(), asset2.hashCode()); // Same ID should have same hashCode
        assertNotEquals(asset1.hashCode(), asset3.hashCode()); // Different ID should have different hashCode
    }

    @Test
    void shouldImplementToStringCorrectly() {
        // Arrange
        String id = UUID.randomUUID().toString();
        String externalId = UUID.randomUUID().toString();
        String type = "book";

        Asset asset = Asset.builder()
                .id(id)
                .externalId(externalId)
                .type(type)
                .build();

        // Act
        String toString = asset.toString();

        // Assert
        assertNotNull(toString);
        assertFalse(toString.isEmpty());
        assertTrue(toString.contains("Asset"), "toString should contain class name");

        boolean containsFields = toString.contains("id=") ||
                toString.contains("externalId=") ||
                toString.contains("type=");

        assertTrue(containsFields, "toString should include at least one field name");
    }

    @Test
    void shouldUseSettersAndGetters() {
        // Arrange
        Asset asset = new Asset();
        String id = UUID.randomUUID().toString();
        String externalId = UUID.randomUUID().toString();
        String type = "movie";
        Asset.PermissionPolicy permissionPolicy = Asset.PermissionPolicy.VIEWER;
        LocalDateTime now = LocalDateTime.now();

        Asset.ArtifactInformation artifactInfo = Asset.ArtifactInformation.builder()
                .groupId("com.tracktainment")
                .artifactId("test-artifact")
                .version("1.0.0")
                .build();

        // Act
        asset.setId(id);
        asset.setExternalId(externalId);
        asset.setType(type);
        asset.setPermissionPolicy(permissionPolicy);
        asset.setArtifactInformation(artifactInfo);
        asset.setCreatedAt(now);
        asset.setUpdatedAt(now);

        // Assert
        assertEquals(id, asset.getId());
        assertEquals(externalId, asset.getExternalId());
        assertEquals(type, asset.getType());
        assertEquals(permissionPolicy, asset.getPermissionPolicy());
        assertEquals(artifactInfo, asset.getArtifactInformation());
        assertEquals(now, asset.getCreatedAt());
        assertEquals(now, asset.getUpdatedAt());
    }

    @Test
    void shouldCreateArtifactInformationUsingBuilder() {
        // Arrange
        String groupId = "com.tracktainment";
        String artifactId = "test-artifact";
        String version = "1.0.0";

        // Act
        Asset.ArtifactInformation artifactInfo = Asset.ArtifactInformation.builder()
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
        Asset.ArtifactInformation artifactInfo = new Asset.ArtifactInformation();

        // Assert
        assertNull(artifactInfo.getGroupId());
        assertNull(artifactInfo.getArtifactId());
        assertNull(artifactInfo.getVersion());
    }

    @Test
    void permissionPolicyShouldHaveCorrectValues() {
        // Assert
        assertEquals("owner", Asset.PermissionPolicy.OWNER.getValue());
        assertEquals("viewer", Asset.PermissionPolicy.VIEWER.getValue());
        assertEquals(2, Asset.PermissionPolicy.values().length);
    }

    @Test
    void permissionPolicyShouldHaveCorrectToString() {
        // Assert
        assertTrue(Asset.PermissionPolicy.OWNER.toString().contains("OWNER"));
        assertTrue(Asset.PermissionPolicy.VIEWER.toString().contains("VIEWER"));
    }
}
