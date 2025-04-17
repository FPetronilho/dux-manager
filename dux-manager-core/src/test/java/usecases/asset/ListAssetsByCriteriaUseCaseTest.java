package usecases.asset;

import com.tracktainment.duxmanager.dataprovider.AssetDataProvider;
import com.tracktainment.duxmanager.domain.Asset;
import com.tracktainment.duxmanager.exception.AuthenticationFailedException;
import com.tracktainment.duxmanager.exception.ParameterValidationErrorException;
import com.tracktainment.duxmanager.security.DigitalUserSecurityContext;
import com.tracktainment.duxmanager.security.SecurityUtil;
import com.tracktainment.duxmanager.usecases.asset.ListAssetsByCriteriaUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import util.test.TestAssetDataUtil;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListAssetsByCriteriaUseCaseTest {

    @Mock
    private AssetDataProvider assetDataProvider;

    @Mock
    private SecurityUtil securityUtil;

    @InjectMocks
    private ListAssetsByCriteriaUseCase listAssetsByCriteriaUseCase;

    private Asset asset1;
    private Asset asset2;
    private DigitalUserSecurityContext digitalUserSecurityContext;
    private ListAssetsByCriteriaUseCase.Input validInput;

    @BeforeEach
    void setUp() {
        asset1 = TestAssetDataUtil.createTestAsset1();
        asset2 = TestAssetDataUtil.createTestAsset2();
        digitalUserSecurityContext = TestAssetDataUtil.createTestDigitalUserSecurityContext();

        // Setup valid input
        validInput = ListAssetsByCriteriaUseCase.Input.builder()
                .offset(0)
                .limit(10)
                .digitalUserId(digitalUserSecurityContext.getId())
                .build();
    }

    @Test
    void shouldListAssetsByCriteriaSuccessfully() {
        // Arrange
        List<Asset> assets = Arrays.asList(asset1, asset2);
        when(securityUtil.getDigitalUser())
                .thenReturn(digitalUserSecurityContext);

        when(assetDataProvider.listByCriteria(validInput))
                .thenReturn(assets);

        // Act
        ListAssetsByCriteriaUseCase.Output output = listAssetsByCriteriaUseCase.execute(validInput);

        // Assert
        assertNotNull(output);
        assertNotNull(output.getAssets());
        assertEquals(2, output.getAssets().size());
        assertTrue(output.getAssets().contains(asset1));
        assertTrue(output.getAssets().contains(asset2));

        verify(securityUtil).getDigitalUser();
        verify(assetDataProvider).listByCriteria(validInput);
    }

    @Test
    void shouldReturnEmptyListWhenNoAssetsFound() {
        // Arrange
        when(securityUtil.getDigitalUser())
                .thenReturn(digitalUserSecurityContext);

        when(assetDataProvider.listByCriteria(validInput))
                .thenReturn(Collections.emptyList());

        // Act
        ListAssetsByCriteriaUseCase.Output output = listAssetsByCriteriaUseCase.execute(validInput);

        // Assert
        assertNotNull(output);
        assertNotNull(output.getAssets());
        assertTrue(output.getAssets().isEmpty());

        verify(securityUtil).getDigitalUser();
        verify(assetDataProvider).listByCriteria(validInput);
    }

    @Test
    void shouldThrowAuthenticationFailedExceptionWhenUserIdDoesNotMatch() {
        when(securityUtil.getDigitalUser())
                .thenReturn(digitalUserSecurityContext);

        ListAssetsByCriteriaUseCase.Input input = ListAssetsByCriteriaUseCase.Input.builder()
                .offset(0)
                .limit(10)
                .digitalUserId(UUID.randomUUID().toString()) // Different user ID
                .build();

        // Act & Assert
        assertThrows(AuthenticationFailedException.class, () -> listAssetsByCriteriaUseCase.execute(input));

        verify(securityUtil).getDigitalUser();
        verify(assetDataProvider, never()).listByCriteria(any());
    }

    @Test
    void shouldFilterByTypeCorrectly() {
        // Arrange
        when(securityUtil.getDigitalUser())
                .thenReturn(digitalUserSecurityContext);

        ListAssetsByCriteriaUseCase.Input typeFilterInput = ListAssetsByCriteriaUseCase.Input.builder()
                .offset(0)
                .limit(10)
                .digitalUserId(digitalUserSecurityContext.getId())
                .type("book")
                .build();

        when(assetDataProvider.listByCriteria(typeFilterInput))
                .thenReturn(Collections.singletonList(asset1));

        // Act
        ListAssetsByCriteriaUseCase.Output output = listAssetsByCriteriaUseCase.execute(typeFilterInput);

        // Assert
        assertNotNull(output);
        assertNotNull(output.getAssets());
        assertEquals(1, output.getAssets().size());
        assertEquals("book", output.getAssets().get(0).getType());

        verify(securityUtil).getDigitalUser();
        verify(assetDataProvider).listByCriteria(typeFilterInput);
    }

    @Test
    void shouldFilterByDateRangeCorrectly() {
        // Arrange
        when(securityUtil.getDigitalUser())
                .thenReturn(digitalUserSecurityContext);

        ListAssetsByCriteriaUseCase.Input dateRangeInput = ListAssetsByCriteriaUseCase.Input.builder()
                .offset(0)
                .limit(10)
                .digitalUserId(digitalUserSecurityContext.getId())
                .from(LocalDate.now().minusDays(1))
                .to(LocalDate.now().plusDays(1))
                .build();

        List<Asset> assets = Arrays.asList(asset1, asset2);
        when(assetDataProvider.listByCriteria(dateRangeInput))
                .thenReturn(assets);

        // Act
        ListAssetsByCriteriaUseCase.Output output = listAssetsByCriteriaUseCase.execute(dateRangeInput);

        // Assert
        assertNotNull(output);
        assertNotNull(output.getAssets());
        assertEquals(2, output.getAssets().size());

        verify(securityUtil).getDigitalUser();
        verify(assetDataProvider).listByCriteria(dateRangeInput);
    }

    @Test
    void shouldPropagateExceptionsFromDataProvider() {
        // Arrange
        when(securityUtil.getDigitalUser())
                .thenReturn(digitalUserSecurityContext);

        when(assetDataProvider.listByCriteria(validInput))
                .thenThrow(new ParameterValidationErrorException("Invalid criteria"));

        // Act & Assert
        assertThrows(ParameterValidationErrorException.class, () -> listAssetsByCriteriaUseCase.execute(validInput));

        verify(securityUtil).getDigitalUser();
        verify(assetDataProvider).listByCriteria(validInput);
    }
}