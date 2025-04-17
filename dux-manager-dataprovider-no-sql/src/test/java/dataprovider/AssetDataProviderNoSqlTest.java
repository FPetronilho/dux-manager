package dataprovider;

import com.tracktainment.duxmanager.dataprovider.AssetDataProviderNoSql;
import com.tracktainment.duxmanager.dataprovider.DigitalUserDataProviderNoSql;
import com.tracktainment.duxmanager.document.DigitalUserDocument;
import com.tracktainment.duxmanager.domain.Asset;
import com.tracktainment.duxmanager.dto.AssetCreate;
import com.tracktainment.duxmanager.exception.ParameterValidationErrorException;
import com.tracktainment.duxmanager.exception.ResourceAlreadyExistsException;
import com.tracktainment.duxmanager.exception.ResourceNotFoundException;
import com.tracktainment.duxmanager.mapper.AssetMapperDataProvider;
import com.tracktainment.duxmanager.usecases.asset.ListAssetsByCriteriaUseCase;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import testutil.TestAssetDataUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssetDataProviderNoSqlTest {

    @Mock
    private AssetMapperDataProvider mapper;

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private DigitalUserDataProviderNoSql digitalUserDataProviderNoSql;

    @InjectMocks
    private AssetDataProviderNoSql assetDataProviderNoSql;

    @Captor
    private ArgumentCaptor<Query> queryCaptor;

    @Captor
    private ArgumentCaptor<Update> updateCaptor;

    private AssetCreate assetCreate;
    private Asset asset;
    private DigitalUserDocument digitalUserDocument;

    @BeforeEach
    void setUp() {
        assetCreate = TestAssetDataUtil.createTestAssetCreate1();
        asset = TestAssetDataUtil.createTestAsset1();

        // Setup DigitalUserDocument
        digitalUserDocument = new DigitalUserDocument();
        digitalUserDocument.setId("a23e4567-e89b-12d3-a456-426614174009");
        digitalUserDocument.setAssets(new ArrayList<>());
    }

    @Test
    void shouldCreateAssetSuccessfully() {
        // Arrange
        when(mongoTemplate.exists(any(Query.class), eq(DigitalUserDocument.class)))
                .thenReturn(false);

        when(digitalUserDataProviderNoSql.findDigitalUserDocumentById(digitalUserDocument.getId()))
                .thenReturn(digitalUserDocument);

        when(mapper.toAsset(assetCreate))
                .thenReturn(asset);

        when(mongoTemplate.save(digitalUserDocument))
                .thenReturn(digitalUserDocument);

        // Act
        Asset result = assetDataProviderNoSql.create(digitalUserDocument.getId(), assetCreate);

        // Assert
        assertNotNull(result);
        assertEquals(asset.getId(), result.getId());
        assertEquals(asset.getExternalId(), result.getExternalId());

        verify(mongoTemplate).exists(queryCaptor.capture(), eq(DigitalUserDocument.class));
        verify(digitalUserDataProviderNoSql).findDigitalUserDocumentById(digitalUserDocument.getId());
        verify(mapper).toAsset(assetCreate);
        verify(mongoTemplate).save(digitalUserDocument);

        // Verify asset was added to the user document
        assertEquals(1, digitalUserDocument.getAssets().size());
        assertEquals(asset, digitalUserDocument.getAssets().get(0));
    }

    @Test
    void shouldThrowResourceAlreadyExistsExceptionWhenAssetExists() {
        // Arrange
        when(mongoTemplate.exists(any(Query.class), eq(DigitalUserDocument.class)))
                .thenReturn(true);

        // Act & Assert
        assertThrows(ResourceAlreadyExistsException.class,
                () -> assetDataProviderNoSql.create(digitalUserDocument.getId(), assetCreate));

        verify(mongoTemplate).exists(any(Query.class), eq(DigitalUserDocument.class));
        verify(digitalUserDataProviderNoSql, never()).findDigitalUserDocumentById(anyString());
        verify(mapper, never()).toAsset(any());
        verify(mongoTemplate, never()).save(any());
    }

    @Test
    void shouldFindAssetByExternalIdSuccessfully() {
        // Arrange
        DigitalUserDocument userWithAsset = new DigitalUserDocument();
        userWithAsset.setId(digitalUserDocument.getId());
        userWithAsset.setAssets(Collections.singletonList(asset));

        when(mongoTemplate.findOne(any(Query.class), eq(DigitalUserDocument.class)))
                .thenReturn(userWithAsset);

        // Act
        Asset result = assetDataProviderNoSql.findByExternalId(digitalUserDocument.getId(), asset.getExternalId());

        // Assert
        assertNotNull(result);
        assertEquals(asset.getId(), result.getId());
        assertEquals(asset.getExternalId(), result.getExternalId());

        verify(mongoTemplate).findOne(queryCaptor.capture(), eq(DigitalUserDocument.class));
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenAssetNotFound() {
        // Arrange
        when(mongoTemplate.findOne(any(Query.class), eq(DigitalUserDocument.class)))
                .thenReturn(null);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> assetDataProviderNoSql.findByExternalId(digitalUserDocument.getId(), asset.getExternalId()));

        verify(mongoTemplate).findOne(any(Query.class), eq(DigitalUserDocument.class));
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenUserHasNoAssets() {
        // Arrange
        DigitalUserDocument emptyUserDocument = new DigitalUserDocument();
        emptyUserDocument.setId(digitalUserDocument.getId());
        emptyUserDocument.setAssets(Collections.emptyList());

        when(mongoTemplate.findOne(any(Query.class), eq(DigitalUserDocument.class))).thenReturn(emptyUserDocument);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> assetDataProviderNoSql.findByExternalId(digitalUserDocument.getId(), asset.getExternalId()));

        verify(mongoTemplate).findOne(any(Query.class), eq(DigitalUserDocument.class));
    }

    @Test
    void shouldListAssetsByCriteriaSuccessfully() {
        // Arrange
        Asset asset1 = TestAssetDataUtil.createTestAsset1();
        Asset asset2 = TestAssetDataUtil.createTestAsset2();

        DigitalUserDocument userWithAssets = new DigitalUserDocument();
        userWithAssets.setId(digitalUserDocument.getId());
        userWithAssets.setAssets(Arrays.asList(asset1, asset2));

        ListAssetsByCriteriaUseCase.Input input = ListAssetsByCriteriaUseCase.Input.builder()
                .offset(0)
                .limit(10)
                .digitalUserId(digitalUserDocument.getId())
                .build();

        when(digitalUserDataProviderNoSql.existsById(digitalUserDocument.getId()))
                .thenReturn(true);

        when(mongoTemplate.findOne(any(Query.class), eq(DigitalUserDocument.class)))
                .thenReturn(userWithAssets);

        // Act
        List<Asset> results = assetDataProviderNoSql.listByCriteria(input);

        // Assert
        assertNotNull(results);
        assertEquals(2, results.size());
        assertTrue(results.contains(asset1));
        assertTrue(results.contains(asset2));

        verify(digitalUserDataProviderNoSql).existsById(digitalUserDocument.getId());
        verify(mongoTemplate).findOne(queryCaptor.capture(), eq(DigitalUserDocument.class));
    }

    @Test
    void shouldReturnEmptyListWhenNoAssetsFound() {
        // Arrange
        DigitalUserDocument emptyUserDocument = new DigitalUserDocument();
        emptyUserDocument.setId(digitalUserDocument.getId());
        emptyUserDocument.setAssets(Collections.emptyList());

        ListAssetsByCriteriaUseCase.Input input = ListAssetsByCriteriaUseCase.Input.builder()
                .offset(0)
                .limit(10)
                .digitalUserId(digitalUserDocument.getId())
                .build();

        when(digitalUserDataProviderNoSql.existsById(digitalUserDocument.getId())).thenReturn(true);
        when(mongoTemplate.findOne(any(Query.class), eq(DigitalUserDocument.class))).thenReturn(emptyUserDocument);

        // Act
        List<Asset> results = assetDataProviderNoSql.listByCriteria(input);

        // Assert
        assertNotNull(results);
        assertTrue(results.isEmpty());

        verify(digitalUserDataProviderNoSql).existsById(digitalUserDocument.getId());
        verify(mongoTemplate).findOne(any(Query.class), eq(DigitalUserDocument.class));
    }

    @Test
    void shouldThrowParameterValidationErrorExceptionWhenDigitalUserIdIsNull() {
        // Arrange
        ListAssetsByCriteriaUseCase.Input input = ListAssetsByCriteriaUseCase.Input.builder()
                .offset(0)
                .limit(10)
                .digitalUserId(null)
                .build();

        // Act & Assert
        assertThrows(ParameterValidationErrorException.class,
                () -> assetDataProviderNoSql.listByCriteria(input));
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenUserNotFound() {
        // Arrange
        ListAssetsByCriteriaUseCase.Input input = ListAssetsByCriteriaUseCase.Input.builder()
                .offset(0)
                .limit(10)
                .digitalUserId(digitalUserDocument.getId())
                .build();

        when(digitalUserDataProviderNoSql.existsById(digitalUserDocument.getId()))
                .thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> assetDataProviderNoSql.listByCriteria(input));

        verify(digitalUserDataProviderNoSql).existsById(digitalUserDocument.getId());
        verify(mongoTemplate, never()).findOne(any(Query.class), eq(DigitalUserDocument.class));
    }

    @Test
    void shouldFilterAssetsByType() {
        // Arrange
        Asset asset1 = TestAssetDataUtil.createTestAsset1();
        Asset asset2 = TestAssetDataUtil.createTestAsset2();

        DigitalUserDocument userWithAssets = new DigitalUserDocument();
        userWithAssets.setId(digitalUserDocument.getId());
        userWithAssets.setAssets(Arrays.asList(asset1, asset2));

        ListAssetsByCriteriaUseCase.Input input = ListAssetsByCriteriaUseCase.Input.builder()
                .offset(0)
                .limit(10)
                .digitalUserId(digitalUserDocument.getId())
                .type("book")
                .build();

        when(digitalUserDataProviderNoSql.existsById(digitalUserDocument.getId()))
                .thenReturn(true);

        when(mongoTemplate.findOne(any(Query.class), eq(DigitalUserDocument.class)))
                .thenReturn(userWithAssets);

        // Act
        List<Asset> results = assetDataProviderNoSql.listByCriteria(input);

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("book", results.get(0).getType());

        verify(digitalUserDataProviderNoSql).existsById(digitalUserDocument.getId());
        verify(mongoTemplate).findOne(any(Query.class), eq(DigitalUserDocument.class));
    }

    @Test
    void shouldFilterAssetsByExternalIds() {
        // Arrange
        Asset asset1 = TestAssetDataUtil.createTestAsset1();
        Asset asset2 = TestAssetDataUtil.createTestAsset2();
        String targetExternalId = asset1.getExternalId() + "," + asset2.getExternalId();

        DigitalUserDocument userWithAssets = new DigitalUserDocument();
        userWithAssets.setId(digitalUserDocument.getId());
        userWithAssets.setAssets(Arrays.asList(asset1, asset2));

        ListAssetsByCriteriaUseCase.Input input = ListAssetsByCriteriaUseCase.Input.builder()
                .offset(0)
                .limit(10)
                .digitalUserId(digitalUserDocument.getId())
                .externalIds(targetExternalId)
                .build();

        when(digitalUserDataProviderNoSql.existsById(digitalUserDocument.getId()))
                .thenReturn(true);

        when(mongoTemplate.findOne(any(Query.class), eq(DigitalUserDocument.class)))
                .thenReturn(userWithAssets);

        // Act
        List<Asset> results = assetDataProviderNoSql.listByCriteria(input);

        // Assert
        assertNotNull(results);
        assertEquals(2, results.size());
        assertEquals(asset1.getExternalId(), results.get(0).getExternalId());

        verify(digitalUserDataProviderNoSql).existsById(digitalUserDocument.getId());
        verify(mongoTemplate).findOne(any(Query.class), eq(DigitalUserDocument.class));
    }

    @Test
    void shouldFilterAssetsByArtifactId() {
        // Arrange
        Asset asset1 = TestAssetDataUtil.createTestAsset1();
        Asset asset2 = TestAssetDataUtil.createTestAsset2();

        DigitalUserDocument userWithAssets = new DigitalUserDocument();
        userWithAssets.setId(digitalUserDocument.getId());
        userWithAssets.setAssets(Arrays.asList(asset1, asset2));

        ListAssetsByCriteriaUseCase.Input input = ListAssetsByCriteriaUseCase.Input.builder()
                .offset(0)
                .limit(10)
                .digitalUserId(digitalUserDocument.getId())
                .artifactId("book-manager")
                .build();

        when(digitalUserDataProviderNoSql.existsById(digitalUserDocument.getId()))
                .thenReturn(true);

        when(mongoTemplate.findOne(any(Query.class), eq(DigitalUserDocument.class)))
                .thenReturn(userWithAssets);

        // Act
        List<Asset> results = assetDataProviderNoSql.listByCriteria(input);

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("book-manager", results.get(0).getArtifactInformation().getArtifactId());

        verify(digitalUserDataProviderNoSql).existsById(digitalUserDocument.getId());
        verify(mongoTemplate).findOne(any(Query.class), eq(DigitalUserDocument.class));
    }

    @Test
    void shouldFilterAssetsByCreatedAtDate() {
        // Arrange
        Asset asset1 = TestAssetDataUtil.createTestAsset1();
        Asset asset2 = TestAssetDataUtil.createTestAsset2();

        DigitalUserDocument userWithAssets = new DigitalUserDocument();
        userWithAssets.setId(digitalUserDocument.getId());
        userWithAssets.setAssets(Arrays.asList(asset1, asset2));

        ListAssetsByCriteriaUseCase.Input input = ListAssetsByCriteriaUseCase.Input.builder()
                .offset(0)
                .limit(10)
                .digitalUserId(digitalUserDocument.getId())
                .createdAt(LocalDate.now())
                .build();

        when(digitalUserDataProviderNoSql.existsById(digitalUserDocument.getId()))
                .thenReturn(true);

        when(mongoTemplate.findOne(any(Query.class), eq(DigitalUserDocument.class)))
                .thenReturn(userWithAssets);

        // Act
        List<Asset> results = assetDataProviderNoSql.listByCriteria(input);

        // Assert
        assertNotNull(results);
        assertEquals(2, results.size());
        assertEquals(LocalDate.now().atTime(12, 0), results.get(0).getCreatedAt());

        verify(digitalUserDataProviderNoSql).existsById(digitalUserDocument.getId());
        verify(mongoTemplate).findOne(any(Query.class), eq(DigitalUserDocument.class));
    }

    @Test
    void shouldDeleteAssetSuccessfully() {
        // Arrange
        when(mongoTemplate.exists(any(Query.class), eq(DigitalUserDocument.class)))
                .thenReturn(true);

        when(mongoTemplate.updateFirst(any(Query.class), any(Update.class), eq(DigitalUserDocument.class)))
                .thenReturn(null);

        // Act
        assetDataProviderNoSql.delete(digitalUserDocument.getId(), asset.getExternalId());

        // Assert
        verify(mongoTemplate).exists(queryCaptor.capture(), eq(DigitalUserDocument.class));
        verify(mongoTemplate).updateFirst(any(Query.class), updateCaptor.capture(), eq(DigitalUserDocument.class));

        // Verify the update operation
        Update update = updateCaptor.getValue();
        Document updateObject = update.getUpdateObject();
        assertTrue(updateObject.toJson().contains("$pull"));
        assertTrue(updateObject.toJson().contains("assets"));
        assertTrue(updateObject.toJson().contains(asset.getExternalId()));
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenDeletingNonExistentAsset() {
        // Arrange
        when(mongoTemplate.exists(any(Query.class), eq(DigitalUserDocument.class))).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> assetDataProviderNoSql.delete(digitalUserDocument.getId(), asset.getExternalId()));

        verify(mongoTemplate).exists(any(Query.class), eq(DigitalUserDocument.class));
        verify(mongoTemplate, never()).updateFirst(any(Query.class), any(Update.class), (Class<?>) any());
    }
}