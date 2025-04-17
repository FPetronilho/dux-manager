package mapper;

import com.tracktainment.duxmanager.domain.Asset;
import com.tracktainment.duxmanager.dto.AssetCreate;
import com.tracktainment.duxmanager.mapper.AssetMapperDataProvider;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import testutil.TestAssetDataUtil;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class AssetMapperDataProviderTest {

    private final AssetMapperDataProvider mapper = Mappers.getMapper(AssetMapperDataProvider.class);

    // UUID pattern matcher
    private static final Pattern UUID_PATTERN =
            Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

    @Test
    void shouldMapAssetCreateToAsset() {
        // Arrange
        AssetCreate assetCreate = TestAssetDataUtil.createTestAssetCreate1();

        // Act
        Asset result = mapper.toAsset(assetCreate);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getId());
        assertTrue(UUID_PATTERN.matcher(result.getId()).matches(), "ID should be a valid UUID");
        assertEquals(assetCreate.getExternalId(), result.getExternalId());
            assertEquals(assetCreate.getType(), result.getType());
        assertEquals(assetCreate.getPermissionPolicy().getValue(), result.getPermissionPolicy().getValue());

        assertNotNull(result.getArtifactInformation());
        assertEquals(
                assetCreate.getArtifactInformation().getGroupId(),
                result.getArtifactInformation().getGroupId()
        );
        assertEquals(
                assetCreate.getArtifactInformation().getArtifactId(),
                result.getArtifactInformation().getArtifactId()
        );
        assertEquals(
                assetCreate.getArtifactInformation().getVersion(),
                result.getArtifactInformation().getVersion()
        );

        // Timestamps should be null as specified in the mapping
        assertNull(result.getCreatedAt());
        assertNull(result.getUpdatedAt());
    }

    @Test
    void shouldHandleNullInput() {
        // Act & Assert
        assertNull(mapper.toAsset(null));
    }

    @Test
    void shouldHandleEmptyAssetCreate() {
        // Arrange
        AssetCreate emptyAssetCreate = new AssetCreate();

        // Act
        Asset result = mapper.toAsset(emptyAssetCreate);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getId());
        assertTrue(UUID_PATTERN.matcher(result.getId()).matches(), "ID should be a valid UUID");
        assertNull(result.getExternalId());
        assertNull(result.getType());
        assertNull(result.getPermissionPolicy());
        assertNull(result.getArtifactInformation());
        assertNull(result.getCreatedAt());
        assertNull(result.getUpdatedAt());
    }

    @Test
    void shouldMapArtifactInformation() {
        // Arrange
        AssetCreate.ArtifactInformation artifactInfo = AssetCreate.ArtifactInformation.builder()
                .groupId("com.tracktainment")
                .artifactId("test-artifact")
                .version("1.0.0")
                .build();

        AssetCreate assetCreate = AssetCreate.builder()
                .artifactInformation(artifactInfo)
                .build();

        // Act
        Asset result = mapper.toAsset(assetCreate);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getArtifactInformation());
        assertEquals(artifactInfo.getGroupId(), result.getArtifactInformation().getGroupId());
        assertEquals(artifactInfo.getArtifactId(), result.getArtifactInformation().getArtifactId());
        assertEquals(artifactInfo.getVersion(), result.getArtifactInformation().getVersion());
    }

    @Test
    void shouldGenerateDifferentIdsForDifferentCalls() {
        // Arrange
        AssetCreate assetCreate = AssetCreate.builder().build();

        // Act
        Asset result1 = mapper.toAsset(assetCreate);
        Asset result2 = mapper.toAsset(assetCreate);

        // Assert
        assertNotNull(result1.getId());
        assertNotNull(result2.getId());
        assertNotEquals(result1.getId(), result2.getId(), "Generated IDs should be different");
    }
}
