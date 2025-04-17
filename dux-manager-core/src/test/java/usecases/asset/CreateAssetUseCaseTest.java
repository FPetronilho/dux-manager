package usecases.asset;

import com.tracktainment.duxmanager.dataprovider.AssetDataProvider;
import com.tracktainment.duxmanager.domain.Asset;
import com.tracktainment.duxmanager.dto.AssetCreate;
import com.tracktainment.duxmanager.exception.AuthenticationFailedException;
import com.tracktainment.duxmanager.exception.ResourceAlreadyExistsException;
import com.tracktainment.duxmanager.exception.ResourceNotFoundException;
import com.tracktainment.duxmanager.security.DigitalUserSecurityContext;
import com.tracktainment.duxmanager.security.SecurityUtil;
import com.tracktainment.duxmanager.usecases.asset.CreateAssetUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import util.test.TestAssetDataUtil;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateAssetUseCaseTest {

    @Mock
    private AssetDataProvider assetDataProvider;

    @Mock
    private SecurityUtil securityUtil;

    @InjectMocks
    private CreateAssetUseCase createAssetUseCase;

    private AssetCreate assetCreate;
    private Asset asset;
    private DigitalUserSecurityContext digitalUserSecurityContext;

    @BeforeEach
    void setup() {
        assetCreate = TestAssetDataUtil.createTestAssetCreate1();
        asset = TestAssetDataUtil.createTestAsset1();
        digitalUserSecurityContext = TestAssetDataUtil.createTestDigitalUserSecurityContext();
    }

    @Test
    void shouldCreateAssetSuccessfully() {
        // Arrange
        when(securityUtil.getDigitalUser())
                .thenReturn(digitalUserSecurityContext);

        when(assetDataProvider.create(eq(digitalUserSecurityContext.getId()), any(AssetCreate.class)))
                .thenReturn(asset);

        CreateAssetUseCase.Input input = CreateAssetUseCase.Input.builder()
                .digitalUserId(digitalUserSecurityContext.getId())
                .assetCreate(assetCreate)
                .build();

        // Act
        CreateAssetUseCase.Output output = createAssetUseCase.execute(input);

        // Assert
        assertNotNull(output);
        assertNotNull(output.getAsset());
        assertEquals(asset.getId(), output.getAsset().getId());
        assertEquals(asset.getExternalId(), output.getAsset().getExternalId());
        assertEquals(asset.getType(), output.getAsset().getType());
        assertEquals(asset.getPermissionPolicy(), output.getAsset().getPermissionPolicy());
        assertEquals(
                asset.getArtifactInformation().getArtifactId(),
                output.getAsset().getArtifactInformation().getArtifactId()
        );
        assertEquals(
                asset.getArtifactInformation().getGroupId(),
                output.getAsset().getArtifactInformation().getGroupId()
        );
        assertEquals(
                asset.getArtifactInformation().getVersion(),
                output.getAsset().getArtifactInformation().getVersion()
        );

        verify(securityUtil).getDigitalUser();
        verify(assetDataProvider).create(eq(digitalUserSecurityContext.getId()), eq(assetCreate));
    }

    @Test
    void shouldThrowAuthenticationFailedExceptionWhenUserIdDoesNotMatch() {
        // Arrange
        when(securityUtil.getDigitalUser())
                .thenReturn(digitalUserSecurityContext);

        CreateAssetUseCase.Input input = CreateAssetUseCase.Input.builder()
                .digitalUserId(UUID.randomUUID().toString())
                .assetCreate(assetCreate)
                .build();

        // Act & Assert
        assertThrows(AuthenticationFailedException.class, () -> createAssetUseCase.execute(input));

        verify(securityUtil).getDigitalUser();
        verify(assetDataProvider, never()).create(any(), any());
    }

    @Test
    void shouldPropagateResourceAlreadyExistsExceptionFromDataProvider() {
        // Arrange
        when(securityUtil.getDigitalUser())
                .thenReturn(digitalUserSecurityContext);

        when(assetDataProvider.create(eq(digitalUserSecurityContext.getId()), any(AssetCreate.class)))
                .thenThrow(new ResourceAlreadyExistsException(Asset.class, assetCreate.getExternalId()));

        CreateAssetUseCase.Input input = CreateAssetUseCase.Input.builder()
                .digitalUserId(digitalUserSecurityContext.getId())
                .assetCreate(assetCreate)
                .build();

        // Act & Assert
        assertThrows(ResourceAlreadyExistsException.class, () -> createAssetUseCase.execute(input));

        verify(securityUtil).getDigitalUser();
        verify(assetDataProvider).create(eq(digitalUserSecurityContext.getId()), eq(assetCreate));
    }

    @Test
    void shouldPropagateResourceNotFoundExceptionFromDataProvider() {
        // Arrange
        when(securityUtil.getDigitalUser())
                .thenReturn(digitalUserSecurityContext);

        when(assetDataProvider.create(eq(digitalUserSecurityContext.getId()), any(AssetCreate.class)))
                .thenThrow(new ResourceNotFoundException(Asset.class, UUID.randomUUID().toString()));

        CreateAssetUseCase.Input input = CreateAssetUseCase.Input.builder()
                .digitalUserId(digitalUserSecurityContext.getId())
                .assetCreate(assetCreate)
                .build();

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> createAssetUseCase.execute(input));

        verify(securityUtil).getDigitalUser();
        verify(assetDataProvider).create(eq(digitalUserSecurityContext.getId()), eq(assetCreate));
    }

    @Test
    void shouldHandleSecurityUtilException() {
        // Arrange
        when(securityUtil.getDigitalUser())
                .thenThrow(new AuthenticationFailedException("JWT not found in security context."));

        CreateAssetUseCase.Input input = CreateAssetUseCase.Input.builder()
                .digitalUserId(digitalUserSecurityContext.getId())
                .assetCreate(assetCreate)
                .build();

        // Act & Assert
        assertThrows(AuthenticationFailedException.class, () -> createAssetUseCase.execute(input));

        verify(securityUtil).getDigitalUser();
        verify(assetDataProvider, never()).create(any(), any());
    }
}
