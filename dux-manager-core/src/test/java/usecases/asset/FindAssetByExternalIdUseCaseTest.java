package usecases.asset;

import com.tracktainment.duxmanager.dataprovider.AssetDataProvider;
import com.tracktainment.duxmanager.domain.Asset;
import com.tracktainment.duxmanager.exception.AuthenticationFailedException;
import com.tracktainment.duxmanager.exception.ResourceAlreadyExistsException;
import com.tracktainment.duxmanager.exception.ResourceNotFoundException;
import com.tracktainment.duxmanager.security.DigitalUserSecurityContext;
import com.tracktainment.duxmanager.security.SecurityUtil;
import com.tracktainment.duxmanager.usecases.asset.FindAssetByExternalIdUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import util.test.TestAssetDataUtil;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FindAssetByExternalIdUseCaseTest {

    @Mock
    private AssetDataProvider assetDataProvider;

    @Mock
    private SecurityUtil securityUtil;

    @InjectMocks
    private FindAssetByExternalIdUseCase findAssetByExternalIdUseCase;

    private Asset asset;
    private DigitalUserSecurityContext digitalUserSecurityContext;

    @BeforeEach
    void setup() {
        asset = TestAssetDataUtil.createTestAsset1();
        digitalUserSecurityContext = TestAssetDataUtil.createTestDigitalUserSecurityContext();
    }

    @Test
    void shouldFindAssetByExternalIdSuccessfully() {
        // Arrange
        when(securityUtil.getDigitalUser())
                .thenReturn(digitalUserSecurityContext);

        when(assetDataProvider.findByExternalId(digitalUserSecurityContext.getId(), asset.getExternalId()))
                .thenReturn(asset);

        FindAssetByExternalIdUseCase.Input input = FindAssetByExternalIdUseCase.Input.builder()
                .digitalUserId(digitalUserSecurityContext.getId())
                .externalId(asset.getExternalId())
                .build();

        // Act
        FindAssetByExternalIdUseCase.Output output = findAssetByExternalIdUseCase.execute(input);

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
        verify(assetDataProvider).findByExternalId(digitalUserSecurityContext.getId(), asset.getExternalId());
    }

    @Test
    void shouldThrowAuthenticationFailedExceptionWhenUserIdDoesNotMatch() {
        // Arrange
        when(securityUtil.getDigitalUser()).thenReturn(digitalUserSecurityContext);

        FindAssetByExternalIdUseCase.Input input = FindAssetByExternalIdUseCase.Input.builder()
                .digitalUserId(UUID.randomUUID().toString()) // Different user ID
                .externalId(asset.getExternalId())
                .build();

        // Act & Assert
        assertThrows(AuthenticationFailedException.class, () -> findAssetByExternalIdUseCase.execute(input));

        verify(securityUtil).getDigitalUser();
        verify(assetDataProvider, never()).findByExternalId(any(), any());
    }

    @Test
    void shouldPropagateResourceAlreadyExistsExceptionFromDataProvider() {
        // Arrange
        when(securityUtil.getDigitalUser())
                .thenReturn(digitalUserSecurityContext);

        when(assetDataProvider.findByExternalId(eq(digitalUserSecurityContext.getId()), eq(asset.getExternalId())))
                .thenThrow(new ResourceAlreadyExistsException(Asset.class, asset.getExternalId()));

        FindAssetByExternalIdUseCase.Input input = FindAssetByExternalIdUseCase.Input.builder()
                .digitalUserId(digitalUserSecurityContext.getId())
                .externalId(asset.getExternalId())
                .build();

        // Act & Assert
        assertThrows(ResourceAlreadyExistsException.class, () -> findAssetByExternalIdUseCase.execute(input));

        verify(securityUtil).getDigitalUser();
        verify(assetDataProvider).findByExternalId(eq(digitalUserSecurityContext.getId()), eq(asset.getExternalId()));
    }

    @Test
    void shouldPropagateResourceNotFoundException() {
        // Arrange
        when(securityUtil.getDigitalUser())
                .thenReturn(digitalUserSecurityContext);

        when(assetDataProvider.findByExternalId(digitalUserSecurityContext.getId(), asset.getExternalId()))
                .thenThrow(new ResourceNotFoundException(Asset.class, asset.getExternalId()));

        FindAssetByExternalIdUseCase.Input input = FindAssetByExternalIdUseCase.Input.builder()
                .digitalUserId(digitalUserSecurityContext.getId())
                .externalId(asset.getExternalId())
                .build();

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> findAssetByExternalIdUseCase.execute(input));

        verify(securityUtil).getDigitalUser();
        verify(assetDataProvider).findByExternalId(digitalUserSecurityContext.getId(), asset.getExternalId());
    }

    @Test
    void shouldHandleSecurityUtilException() {
        // Arrange
        when(securityUtil.getDigitalUser())
                .thenThrow(new AuthenticationFailedException("JWT not found in security context."));

        FindAssetByExternalIdUseCase.Input input = FindAssetByExternalIdUseCase.Input.builder()
                .digitalUserId(digitalUserSecurityContext.getId())
                .externalId(asset.getExternalId())
                .build();

        // Act & Assert
        assertThrows(AuthenticationFailedException.class, () -> findAssetByExternalIdUseCase.execute(input));

        verify(securityUtil).getDigitalUser();
        verify(assetDataProvider, never()).findByExternalId(any(), any());
    }
}
