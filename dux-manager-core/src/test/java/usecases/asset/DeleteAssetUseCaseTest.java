package usecases.asset;

import com.tracktainment.duxmanager.dataprovider.AssetDataProvider;
import com.tracktainment.duxmanager.domain.Asset;
import com.tracktainment.duxmanager.exception.AuthenticationFailedException;
import com.tracktainment.duxmanager.exception.ResourceNotFoundException;
import com.tracktainment.duxmanager.security.context.DigitalUserSecurityContext;
import com.tracktainment.duxmanager.security.util.SecurityUtil;
import com.tracktainment.duxmanager.usecases.asset.DeleteAssetUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import util.TestAssetDataUtil;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteAssetUseCaseTest {

    @Mock
    private AssetDataProvider assetDataProvider;

    @Mock
    private SecurityUtil securityUtil;

    @InjectMocks
    private DeleteAssetUseCase deleteAssetUseCase;

    private DigitalUserSecurityContext digitalUserSecurityContext;
    private String externalId;

    @BeforeEach
    void setUp() {
        externalId = UUID.randomUUID().toString();
        digitalUserSecurityContext = TestAssetDataUtil.createTestDigitalUserSecurityContext();
    }

    @Test
    void shouldDeleteAssetSuccessfully() {
        // Arrange
        when(securityUtil.getDigitalUser())
                .thenReturn(digitalUserSecurityContext);

        doNothing().when(assetDataProvider).delete(digitalUserSecurityContext.getId(), externalId);

        DeleteAssetUseCase.Input input = DeleteAssetUseCase.Input.builder()
                .digitalUserId(digitalUserSecurityContext.getId())
                .externalId(externalId)
                .build();

        // Act
        assertDoesNotThrow(() -> deleteAssetUseCase.execute(input));

        // Assert
        verify(securityUtil).getDigitalUser();
        verify(assetDataProvider).delete(digitalUserSecurityContext.getId(), externalId);
    }

    @Test
    void shouldThrowAuthenticationFailedExceptionWhenUserIdDoesNotMatch() {
        // Arrange
        when(securityUtil.getDigitalUser())
                .thenReturn(digitalUserSecurityContext);

        DeleteAssetUseCase.Input input = DeleteAssetUseCase.Input.builder()
                .digitalUserId(UUID.randomUUID().toString())
                .externalId(externalId)
                .build();

        // Act & Assert
        assertThrows(AuthenticationFailedException.class, () -> deleteAssetUseCase.execute(input));

        verify(securityUtil).getDigitalUser();
        verify(assetDataProvider, never()).delete(any(), any());
    }

    @Test
    void shouldPropagateResourceNotFoundException() {
        // Arrange
        when(securityUtil.getDigitalUser())
                .thenReturn(digitalUserSecurityContext);

        doThrow(new ResourceNotFoundException(Asset.class, externalId))
                .when(assetDataProvider).delete(digitalUserSecurityContext.getId(), externalId);

        DeleteAssetUseCase.Input input = DeleteAssetUseCase.Input.builder()
                .digitalUserId(digitalUserSecurityContext.getId())
                .externalId(externalId)
                .build();

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> deleteAssetUseCase.execute(input));

        verify(securityUtil).getDigitalUser();
        verify(assetDataProvider).delete(digitalUserSecurityContext.getId(), externalId);
    }

    @Test
    void shouldHandleSecurityUtilException() {
        // Arrange
        when(securityUtil.getDigitalUser())
                .thenThrow(new AuthenticationFailedException("JWT not found in security context."));

        DeleteAssetUseCase.Input input = DeleteAssetUseCase.Input.builder()
                .digitalUserId(digitalUserSecurityContext.getId())
                .externalId(externalId)
                .build();

        // Act & Assert
        assertThrows(AuthenticationFailedException.class, () -> deleteAssetUseCase.execute(input));

        verify(securityUtil).getDigitalUser();
        verify(assetDataProvider, never()).delete(any(), any());
    }
}
