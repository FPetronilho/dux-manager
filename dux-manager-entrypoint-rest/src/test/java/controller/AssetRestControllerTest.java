package controller;

import com.tracktainment.duxmanager.controller.AssetRestController;
import com.tracktainment.duxmanager.domain.Asset;
import com.tracktainment.duxmanager.dto.AssetCreate;
import com.tracktainment.duxmanager.usecases.asset.CreateAssetUseCase;
import com.tracktainment.duxmanager.usecases.asset.DeleteAssetUseCase;
import com.tracktainment.duxmanager.usecases.asset.ListAssetsByCriteriaUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import testutil.TestAssetDataUtil;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssetRestControllerTest {

    @Mock
    private CreateAssetUseCase createAssetUseCase;

    @Mock
    private ListAssetsByCriteriaUseCase listAssetsByCriteriaUseCase;

    @Mock
    private DeleteAssetUseCase deleteAssetUseCase;

    @InjectMocks
    private AssetRestController assetRestController;

    private final String digitalUserId = UUID.randomUUID().toString();
    private final String assetId = UUID.randomUUID().toString();
    private final String externalId = UUID.randomUUID().toString();

    private AssetCreate assetCreate;
    private Asset asset;

    @BeforeEach
    void setUp() {
        assetCreate = TestAssetDataUtil.createTestAssetCreate1();
        asset = TestAssetDataUtil.createTestAsset1();
    }

    @Test
    void shouldCreateAssetSuccessfully() {
        // Arrange
        CreateAssetUseCase.Output output = CreateAssetUseCase.Output.builder()
                .asset(asset)
                .build();

        when(createAssetUseCase.execute(any(CreateAssetUseCase.Input.class)))
                .thenReturn(output);

        // Act
        ResponseEntity<Asset> response = assetRestController.create(digitalUserId, assetCreate);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(asset, response.getBody());

        verify(createAssetUseCase).execute(any(CreateAssetUseCase.Input.class));
    }

    @Test
    void shouldListAssetsByCriteriaSuccessfully() {
        // Arrange
        Asset asset1 = TestAssetDataUtil.createTestAsset1();
        Asset asset2 = TestAssetDataUtil.createTestAsset2();
        List<Asset> assets = Arrays.asList(asset1, asset2);

        ListAssetsByCriteriaUseCase.Output output = ListAssetsByCriteriaUseCase.Output.builder()
                .assets(assets)
                .build();

        when(listAssetsByCriteriaUseCase.execute(any(ListAssetsByCriteriaUseCase.Input.class)))
                .thenReturn(output);

        // Act
        ResponseEntity<List<Asset>> response = assetRestController.listByCriteria(
                0,
                10,
                digitalUserId,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(assets, response.getBody());
        assertEquals(2, response.getBody().size());

        verify(listAssetsByCriteriaUseCase).execute(any(ListAssetsByCriteriaUseCase.Input.class));
    }

    @Test
    void shouldDeleteAssetSuccessfully() {
        // Arrange
        doNothing().when(deleteAssetUseCase).execute(any(DeleteAssetUseCase.Input.class));

        // Act
        ResponseEntity<Void> response = assetRestController.delete(digitalUserId, externalId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        verify(deleteAssetUseCase).execute(any(DeleteAssetUseCase.Input.class));
    }

    @Test
    void shouldPassCorrectInputToCreateUseCase() {
        // Arrange
        CreateAssetUseCase.Output output = CreateAssetUseCase.Output.builder()
                .asset(asset)
                .build();

        when(createAssetUseCase.execute(any(CreateAssetUseCase.Input.class)))
                .thenReturn(output);

        // Act
        assetRestController.create(digitalUserId, assetCreate);

        // Assert
        verify(createAssetUseCase).execute(argThat(input ->
                input.getDigitalUserId().equals(digitalUserId) &&
                        input.getAssetCreate().equals(assetCreate)));
    }

    @Test
    void shouldPassCorrectInputToListByCriteriaUseCase() {
        // Arrange
        Integer offset = 5;
        Integer limit = 20;
        String externalIds = UUID.randomUUID().toString();
        String groupId = "com.test";
        String artifactId = "test-artifact";
        String type = "book";
        LocalDate createdAt = LocalDate.now();
        LocalDate from = LocalDate.now().minusDays(7);
        LocalDate to = LocalDate.now();

        ListAssetsByCriteriaUseCase.Output output = ListAssetsByCriteriaUseCase.Output.builder()
                .assets(Arrays.asList(asset))
                .build();

        when(listAssetsByCriteriaUseCase.execute(any(ListAssetsByCriteriaUseCase.Input.class)))
                .thenReturn(output);

        // Act
        assetRestController.listByCriteria(offset, limit, digitalUserId, externalIds,
                groupId, artifactId, type, createdAt, from, to);

        // Assert
        verify(listAssetsByCriteriaUseCase).execute(argThat(input ->
                input.getOffset().equals(offset) &&
                        input.getLimit().equals(limit) &&
                        input.getDigitalUserId().equals(digitalUserId) &&
                        input.getExternalIds().equals(externalIds) &&
                        input.getGroupId().equals(groupId) &&
                        input.getArtifactId().equals(artifactId) &&
                        input.getType().equals(type) &&
                        input.getCreatedAt().equals(createdAt) &&
                        input.getFrom().equals(from) &&
                        input.getTo().equals(to)));
    }

    @Test
    void shouldPassCorrectInputToDeleteUseCase() {
        // Arrange
        doNothing().when(deleteAssetUseCase).execute(any(DeleteAssetUseCase.Input.class));

        // Act
        assetRestController.delete(digitalUserId, externalId);

        // Assert
        verify(deleteAssetUseCase).execute(argThat(input ->
                input.getDigitalUserId().equals(digitalUserId) &&
                        input.getExternalId().equals(externalId)));
    }
}
