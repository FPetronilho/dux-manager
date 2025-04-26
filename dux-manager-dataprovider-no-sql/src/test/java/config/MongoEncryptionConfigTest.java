package config;

import com.tracktainment.duxmanager.annotation.Encrypted;
import com.tracktainment.duxmanager.config.MongoEncryptionConfig;
import com.tracktainment.duxmanager.encryption.EncryptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MongoEncryptionConfigTest {

    @Mock
    private EncryptionService encryptionService;

    private MongoEncryptionConfig mongoEncryptionConfig;
    private MongoEncryptionConfig.EncryptionMongoEventListener listener;

    @Captor
    private ArgumentCaptor<String> encryptionCaptor;

    @BeforeEach
    void setUp() {
        mongoEncryptionConfig = new MongoEncryptionConfig(encryptionService);
        listener = mongoEncryptionConfig.encryptionMongoEventListener();
    }

    @Test
    void shouldEncryptAnnotatedStringFields() {
        // Arrange
        TestClass testObject = new TestClass();
        testObject.setEncryptedField("sensitive data");
        testObject.setNormalField("normal data");

        BeforeConvertEvent<Object> event = new BeforeConvertEvent<>(testObject, "collection");

        when(encryptionService.encrypt(anyString())).thenReturn("encrypted-data");

        // Act
        listener.onBeforeConvert(event);

        // Assert
        verify(encryptionService).encrypt(encryptionCaptor.capture());
        assertEquals("sensitive data", encryptionCaptor.getValue());
        assertEquals("encrypted-data", testObject.getEncryptedField());
        assertEquals("normal data", testObject.getNormalField());
    }

    @Test
    void shouldHandleNullFieldValues() {
        // Arrange
        TestClass testObject = new TestClass();
        testObject.setEncryptedField(null);
        testObject.setNormalField("normal data");

        BeforeConvertEvent<Object> event = new BeforeConvertEvent<>(testObject, "collection");

        // Act
        listener.onBeforeConvert(event);

        // Assert
        verify(encryptionService, never()).encrypt(anyString());
        assertNull(testObject.getEncryptedField());
        assertEquals("normal data", testObject.getNormalField());
    }

    @Test
    void shouldHandleNestedObjects() {
        // Arrange
        TestClass testObject = new TestClass();
        NestedTestClass nestedObject = new NestedTestClass();
        nestedObject.setEncryptedNestedField("nested sensitive data");
        testObject.setNestedObject(nestedObject);

        BeforeConvertEvent<Object> event = new BeforeConvertEvent<>(testObject, "collection");

        when(encryptionService.encrypt(anyString())).thenReturn("encrypted-nested-data");

        // Act
        listener.onBeforeConvert(event);

        // Assert
        verify(encryptionService).encrypt(encryptionCaptor.capture());
        assertEquals("nested sensitive data", encryptionCaptor.getValue());
        assertEquals("encrypted-nested-data", nestedObject.getEncryptedNestedField());
    }

    @Test
    void shouldHandleCollections() {
        // Arrange
        TestClass testObject = new TestClass();
        NestedTestClass nestedObject1 = new NestedTestClass();
        nestedObject1.setEncryptedNestedField("nested data 1");
        NestedTestClass nestedObject2 = new NestedTestClass();
        nestedObject2.setEncryptedNestedField("nested data 2");

        List<NestedTestClass> nestedList = new ArrayList<>();
        nestedList.add(nestedObject1);
        nestedList.add(nestedObject2);
        testObject.setNestedList(nestedList);

        BeforeConvertEvent<Object> event = new BeforeConvertEvent<>(testObject, "collection");

        when(encryptionService.encrypt("nested data 1")).thenReturn("encrypted-nested-data-1");
        when(encryptionService.encrypt("nested data 2")).thenReturn("encrypted-nested-data-2");

        // Act
        listener.onBeforeConvert(event);

        // Assert
        verify(encryptionService, times(2)).encrypt(anyString());
        assertEquals("encrypted-nested-data-1", nestedObject1.getEncryptedNestedField());
        assertEquals("encrypted-nested-data-2", nestedObject2.getEncryptedNestedField());
    }

    @Test
    void shouldHandleMaps() {
        // Arrange
        TestClass testObject = new TestClass();
        NestedTestClass nestedObject1 = new NestedTestClass();
        nestedObject1.setEncryptedNestedField("nested data 1");
        NestedTestClass nestedObject2 = new NestedTestClass();
        nestedObject2.setEncryptedNestedField("nested data 2");

        Map<String, NestedTestClass> nestedMap = new HashMap<>();
        nestedMap.put("key1", nestedObject1);
        nestedMap.put("key2", nestedObject2);
        testObject.setNestedMap(nestedMap);

        BeforeConvertEvent<Object> event = new BeforeConvertEvent<>(testObject, "collection");

        when(encryptionService.encrypt("nested data 1")).thenReturn("encrypted-nested-data-1");
        when(encryptionService.encrypt("nested data 2")).thenReturn("encrypted-nested-data-2");

        // Act
        listener.onBeforeConvert(event);

        // Assert
        verify(encryptionService, times(2)).encrypt(anyString());
        assertEquals("encrypted-nested-data-1", nestedObject1.getEncryptedNestedField());
        assertEquals("encrypted-nested-data-2", nestedObject2.getEncryptedNestedField());
    }

    @Test
    void shouldNotEncryptNonStringFields() throws Exception {
        // Arrange
        NonStringFieldClass testObject = new NonStringFieldClass();
        testObject.setEncryptedLong(123L);

        BeforeConvertEvent<Object> event = new BeforeConvertEvent<>(testObject, "collection");

        // Act
        listener.onBeforeConvert(event);

        // Assert
        verify(encryptionService, never()).encrypt(anyString());
        assertEquals(123L, testObject.getEncryptedLong());
    }

    @Test
    void shouldEncryptInheritedFields() {
        // Arrange
        ChildClass childObject = new ChildClass();
        childObject.setEncryptedField("parent field");
        childObject.setChildEncryptedField("child field");

        BeforeConvertEvent<Object> event = new BeforeConvertEvent<>(childObject, "collection");

        when(encryptionService.encrypt("parent field")).thenReturn("encrypted-parent-field");
        when(encryptionService.encrypt("child field")).thenReturn("encrypted-child-field");

        // Act
        listener.onBeforeConvert(event);

        // Assert
        verify(encryptionService, times(2)).encrypt(anyString());
        assertEquals("encrypted-parent-field", childObject.getEncryptedField());
        assertEquals("encrypted-child-field", childObject.getChildEncryptedField());
    }

    // Test classes
    static class TestClass {
        @Encrypted
        private String encryptedField;
        private String normalField;
        private NestedTestClass nestedObject;
        private List<NestedTestClass> nestedList;
        private Map<String, NestedTestClass> nestedMap;

        public String getEncryptedField() {
            return encryptedField;
        }

        public void setEncryptedField(String encryptedField) {
            this.encryptedField = encryptedField;
        }

        public String getNormalField() {
            return normalField;
        }

        public void setNormalField(String normalField) {
            this.normalField = normalField;
        }

        public NestedTestClass getNestedObject() {
            return nestedObject;
        }

        public void setNestedObject(NestedTestClass nestedObject) {
            this.nestedObject = nestedObject;
        }

        public List<NestedTestClass> getNestedList() {
            return nestedList;
        }

        public void setNestedList(List<NestedTestClass> nestedList) {
            this.nestedList = nestedList;
        }

        public Map<String, NestedTestClass> getNestedMap() {
            return nestedMap;
        }

        public void setNestedMap(Map<String, NestedTestClass> nestedMap) {
            this.nestedMap = nestedMap;
        }
    }

    static class NestedTestClass {
        @Encrypted
        private String encryptedNestedField;

        public String getEncryptedNestedField() {
            return encryptedNestedField;
        }

        public void setEncryptedNestedField(String encryptedNestedField) {
            this.encryptedNestedField = encryptedNestedField;
        }
    }

    static class CyclicTestClass {
        @Encrypted
        private String encryptedField;
        private CyclicTestClass reference;

        public String getEncryptedField() {
            return encryptedField;
        }

        public void setEncryptedField(String encryptedField) {
            this.encryptedField = encryptedField;
        }

        public CyclicTestClass getReference() {
            return reference;
        }

        public void setReference(CyclicTestClass reference) {
            this.reference = reference;
        }
    }

    static class NonStringFieldClass {
        @Encrypted
        private Long encryptedLong;

        public Long getEncryptedLong() {
            return encryptedLong;
        }

        public void setEncryptedLong(Long encryptedLong) {
            this.encryptedLong = encryptedLong;
        }
    }

    static class ParentClass {
        @Encrypted
        private String encryptedField;

        public String getEncryptedField() {
            return encryptedField;
        }

        public void setEncryptedField(String encryptedField) {
            this.encryptedField = encryptedField;
        }
    }

    static class ChildClass extends ParentClass {
        @Encrypted
        private String childEncryptedField;

        public String getChildEncryptedField() {
            return childEncryptedField;
        }

        public void setChildEncryptedField(String childEncryptedField) {
            this.childEncryptedField = childEncryptedField;
        }
    }
}
