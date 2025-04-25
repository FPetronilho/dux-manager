package dataprovider;

import com.mongodb.client.result.DeleteResult;
import com.tracktainment.duxmanager.dataprovider.DigitalUserDataProviderNoSql;
import com.tracktainment.duxmanager.document.DigitalUserDocument;
import com.tracktainment.duxmanager.domain.DigitalUser;
import com.tracktainment.duxmanager.dto.DigitalUserCreate;
import com.tracktainment.duxmanager.exception.ResourceAlreadyExistsException;
import com.tracktainment.duxmanager.exception.ResourceNotFoundException;
import com.tracktainment.duxmanager.mapper.DigitalUserMapperDataProvider;
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
import testutil.TestDigitalUserDataUtil;
import testutil.TestDigitalUserDocumentDataUtil;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DigitalUserDataProviderNoSqlTest {

    @Mock
    private DigitalUserMapperDataProvider mapper;

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private DigitalUserDataProviderNoSql digitalUserDataProviderNoSql;

    @Captor
    private ArgumentCaptor<Query> queryCaptor;

    @Captor
    private ArgumentCaptor<DigitalUserDocument> documentCaptor;

    private DigitalUserCreate digitalUserCreate;
    private DigitalUser digitalUser;
    private DigitalUserDocument digitalUserDocument;

    @BeforeEach
    void setUp() {
        digitalUserCreate = TestDigitalUserDataUtil.createTestDigitalUserCreate();
        digitalUser = TestDigitalUserDataUtil.createTestDigitalUser();
        digitalUserDocument = TestDigitalUserDocumentDataUtil.createTestDigitalUserDocument();
    }

    @Test
    void shouldCreateDigitalUserSuccessfully() {
        // Arrange
        when(mongoTemplate.exists(any(Query.class), eq(DigitalUserDocument.class))).thenReturn(false);
        when(mapper.toDigitalUserDocument(digitalUserCreate)).thenReturn(digitalUserDocument);
        when(mongoTemplate.save(digitalUserDocument)).thenReturn(digitalUserDocument);
        when(mapper.toDigitalUser(digitalUserDocument)).thenReturn(digitalUser);

        // Act
        DigitalUser result = digitalUserDataProviderNoSql.create(digitalUserCreate);

        // Assert
        assertNotNull(result);
        assertEquals(digitalUser.getId(), result.getId());

        verify(mongoTemplate).exists(queryCaptor.capture(), eq(DigitalUserDocument.class));
        verify(mapper).toDigitalUserDocument(digitalUserCreate);
        verify(mongoTemplate).save(digitalUserDocument);
        verify(mapper).toDigitalUser(digitalUserDocument);
    }

    @Test
    void shouldThrowResourceAlreadyExistsExceptionWhenUserExists() {
        // Arrange
        when(mongoTemplate.exists(any(Query.class), eq(DigitalUserDocument.class)))
                .thenReturn(true);

        // Act & Assert
        assertThrows(ResourceAlreadyExistsException.class, () -> digitalUserDataProviderNoSql.create(digitalUserCreate));

        verify(mongoTemplate).exists(any(Query.class), eq(DigitalUserDocument.class));
        verify(mapper, never()).toDigitalUserDocument(any());
        verify(mongoTemplate, never()).save(any());
    }

    @Test
    void shouldFindDigitalUserByIdSuccessfully() {
        // Arrange
        when(mongoTemplate.findOne(any(Query.class), eq(DigitalUserDocument.class)))
                .thenReturn(digitalUserDocument);

        when(mapper.toDigitalUser(digitalUserDocument))
                .thenReturn(digitalUser);

        // Act
        DigitalUser result = digitalUserDataProviderNoSql.findById(digitalUser.getId());

        // Assert
        assertNotNull(result);
        assertEquals(digitalUser.getId(), result.getId());

        verify(mongoTemplate).findOne(queryCaptor.capture(), eq(DigitalUserDocument.class));
        verify(mapper).toDigitalUser(digitalUserDocument);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenUserNotFound() {
        // Arrange
        when(mongoTemplate.findOne(any(Query.class), eq(DigitalUserDocument.class)))
                .thenReturn(null);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> digitalUserDataProviderNoSql.findById(digitalUser.getId()));

        verify(mongoTemplate).findOne(any(Query.class), eq(DigitalUserDocument.class));
        verify(mapper, never()).toDigitalUser(any());
    }

    @Test
    void shouldFindDigitalUserBySubAndIdPAndTenantSuccessfully() {
        // Arrange
        when(mongoTemplate.findOne(any(Query.class), eq(DigitalUserDocument.class)))
                .thenReturn(digitalUserDocument);

        when(mapper.toDigitalUser(digitalUserDocument))
                .thenReturn(digitalUser);

        // Act
        DigitalUser result = digitalUserDataProviderNoSql.findBySubAndIdPAndTenant(
                digitalUser.getIdentityProviderInformation().getSubject(),
                digitalUser.getIdentityProviderInformation().getIdentityProvider(),
                digitalUser.getIdentityProviderInformation().getTenantId()
        );

        // Assert
        assertNotNull(result);
        assertEquals(digitalUser.getId(), result.getId());
        assertEquals(
                digitalUser.getIdentityProviderInformation().getSubject(),
                result.getIdentityProviderInformation().getSubject()
        );
        assertEquals(
                digitalUser.getIdentityProviderInformation().getIdentityProvider(),
                result.getIdentityProviderInformation().getIdentityProvider()
        );
        assertEquals(
                digitalUser.getIdentityProviderInformation().getTenantId(),
                result.getIdentityProviderInformation().getTenantId()
        );

        verify(mongoTemplate).findOne(queryCaptor.capture(), eq(DigitalUserDocument.class));
        verify(mapper).toDigitalUser(digitalUserDocument);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenUserNotFoundBySubAndIdPAndTenant() {
        // Arrange
        when(mongoTemplate.findOne(any(Query.class), eq(DigitalUserDocument.class)))
                .thenReturn(null);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> digitalUserDataProviderNoSql.findBySubAndIdPAndTenant(
                        digitalUser.getIdentityProviderInformation().getSubject(),
                        digitalUser.getIdentityProviderInformation().getIdentityProvider(),
                        digitalUser.getIdentityProviderInformation().getTenantId()
                ));

        verify(mongoTemplate).findOne(any(Query.class), eq(DigitalUserDocument.class));
        verify(mapper, never()).toDigitalUser(any());
    }

    @Test
    void shouldDeleteDigitalUserSuccessfully() {
        // Arrange
        DeleteResult deleteResult = mock(DeleteResult.class);
        when(deleteResult.getDeletedCount())
                .thenReturn(1L);

        when(mongoTemplate.remove(any(Query.class), eq(DigitalUserDocument.class)))
                .thenReturn(deleteResult);

        // Act
        digitalUserDataProviderNoSql.delete(digitalUser.getId());

        // Assert
        verify(mongoTemplate).remove(queryCaptor.capture(), eq(DigitalUserDocument.class));
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenDeletingNonExistentUser() {
        // Arrange
        DeleteResult deleteResult = mock(DeleteResult.class);
        when(deleteResult.getDeletedCount())
                .thenReturn(0L);

        when(mongoTemplate.remove(any(Query.class), eq(DigitalUserDocument.class)))
                .thenReturn(deleteResult);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> digitalUserDataProviderNoSql.delete(digitalUser.getId()));

        verify(mongoTemplate).remove(any(Query.class), eq(DigitalUserDocument.class));
    }

    @Test
    void shouldCheckExistenceByIdCorrectly() {
        // Arrange
        when(mongoTemplate.exists(any(Query.class), eq(DigitalUserDocument.class)))
                .thenReturn(true);

        // Act
        boolean exists = digitalUserDataProviderNoSql.existsById(digitalUser.getId());

        // Assert
        assertTrue(exists);

        verify(mongoTemplate).exists(queryCaptor.capture(), eq(DigitalUserDocument.class));
    }

    @Test
    void shouldFindDigitalUserDocumentByIdSuccessfully() {
        // Arrange
        when(mongoTemplate.findOne(any(Query.class), eq(DigitalUserDocument.class)))
                .thenReturn(digitalUserDocument);

        // Act
        DigitalUserDocument result = digitalUserDataProviderNoSql.findDigitalUserDocumentById(digitalUser.getId());

        // Assert
        assertNotNull(result);
        assertEquals(digitalUser.getId(), result.getId());

        verify(mongoTemplate).findOne(queryCaptor.capture(), eq(DigitalUserDocument.class));
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenDocumentNotFound() {
        // Arrange
        when(mongoTemplate.findOne(any(Query.class), eq(DigitalUserDocument.class)))
                .thenReturn(null);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> digitalUserDataProviderNoSql.findDigitalUserDocumentById(digitalUser.getId()));

        verify(mongoTemplate).findOne(any(Query.class), eq(DigitalUserDocument.class));
    }
}
