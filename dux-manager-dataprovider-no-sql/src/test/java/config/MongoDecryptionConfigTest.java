package config;

import com.tracktainment.duxmanager.annotation.Encrypted;
import com.tracktainment.duxmanager.config.MongoDecryptionConfig;
import com.tracktainment.duxmanager.encryption.EncryptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MongoDecryptionConfigTest {

    @Mock
    private EncryptionService encryptionService;

    private MongoDecryptionConfig mongoDecryptionConfig;
    private MongoDecryptionConfig.DecryptionMongoEventListener listener;

    @Captor
    private ArgumentCaptor<String> decryptionCaptor;

    @BeforeEach
    void setUp() {
        mongoDecryptionConfig = new MongoDecryptionConfig(encryptionService);
        listener = mongoDecryptionConfig.decryptionMongoEventListener();
    }

    @Test
    void shouldDecryptAnnotatedStringFields() throws Exception {
        // Arrange
        TestClass testObject = new TestClass();
        testObject.setEncryptedField("encrypted-data");
        testObject.setNormalField("normal data");

        // Call processDecryptedFields directly since we can't create a proper AfterConvertEvent
        Method processDecryptedFields = MongoDecryptionConfig.DecryptionMongoEventListener.class
                .getDeclaredMethod("processDecryptedFields", Object.class, java.util.Set.class);
        processDecryptedFields.setAccessible(true);

        when(encryptionService.decrypt(anyString())).thenReturn("sensitive data");

        // Act
        processDecryptedFields.invoke(listener, testObject, new java.util.HashSet<>());

        // Assert
        verify(encryptionService).decrypt(decryptionCaptor.capture());
        assertEquals("encrypted-data", decryptionCaptor.getValue());
        assertEquals("sensitive data", testObject.getEncryptedField());
        assertEquals("normal data", testObject.getNormalField());
    }

    @Test
    void shouldHandleNullFieldValues() throws Exception {
        // Arrange
        TestClass testObject = new TestClass();
        testObject.setEncryptedField(null);
        testObject.setNormalField("normal data");

        // Call processDecryptedFields directly
        Method processDecryptedFields = MongoDecryptionConfig.DecryptionMongoEventListener.class
                .getDeclaredMethod("processDecryptedFields", Object.class, java.util.Set.class);
        processDecryptedFields.setAccessible(true);

        // Act
        processDecryptedFields.invoke(listener, testObject, new java.util.HashSet<>());

        // Assert
        verify(encryptionService, never()).decrypt(anyString());
        assertNull(testObject.getEncryptedField());
        assertEquals("normal data", testObject.getNormalField());
    }

    @Test
    void shouldHandleNestedObjects() throws Exception {
        // Arrange
        TestClass testObject = new TestClass();
        NestedTestClass nestedObject = new NestedTestClass();
        nestedObject.setEncryptedNestedField("encrypted-nested-data");
        testObject.setNestedObject(nestedObject);

        // Call processDecryptedFields directly
        Method processDecryptedFields = MongoDecryptionConfig.DecryptionMongoEventListener.class
                .getDeclaredMethod("processDecryptedFields", Object.class, java.util.Set.class);
        processDecryptedFields.setAccessible(true);

        when(encryptionService.decrypt(anyString())).thenReturn("nested sensitive data");

        // Act
        processDecryptedFields.invoke(listener, testObject, new java.util.HashSet<>());

        // Assert
        verify(encryptionService).decrypt(decryptionCaptor.capture());
        assertEquals("encrypted-nested-data", decryptionCaptor.getValue());
        assertEquals("nested sensitive data", nestedObject.getEncryptedNestedField());
    }

    @Test
    void shouldHandleCollections() throws Exception {
        // Arrange
        TestClass testObject = new TestClass();
        NestedTestClass nestedObject1 = new NestedTestClass();
        nestedObject1.setEncryptedNestedField("encrypted-nested-data-1");
        NestedTestClass nestedObject2 = new NestedTestClass();
        nestedObject2.setEncryptedNestedField("encrypted-nested-data-2");

        List<NestedTestClass> nestedList = new ArrayList<>();
        nestedList.add(nestedObject1);
        nestedList.add(nestedObject2);
        testObject.setNestedList(nestedList);

        // Call processDecryptedFields directly
        Method processDecryptedFields = MongoDecryptionConfig.DecryptionMongoEventListener.class
                .getDeclaredMethod("processDecryptedFields", Object.class, java.util.Set.class);
        processDecryptedFields.setAccessible(true);

        when(encryptionService.decrypt("encrypted-nested-data-1")).thenReturn("nested data 1");
        when(encryptionService.decrypt("encrypted-nested-data-2")).thenReturn("nested data 2");

        // Act
        processDecryptedFields.invoke(listener, testObject, new java.util.HashSet<>());

        // Assert
        verify(encryptionService, times(2)).decrypt(anyString());
        assertEquals("nested data 1", nestedObject1.getEncryptedNestedField());
        assertEquals("nested data 2", nestedObject2.getEncryptedNestedField());
    }

    @Test
    void shouldHandleMaps() throws Exception {
        // Arrange
        TestClass testObject = new TestClass();
        NestedTestClass nestedObject1 = new NestedTestClass();
        nestedObject1.setEncryptedNestedField("encrypted-nested-data-1");
        NestedTestClass nestedObject2 = new NestedTestClass();
        nestedObject2.setEncryptedNestedField("encrypted-nested-data-2");

        Map<String, NestedTestClass> nestedMap = new HashMap<>();
        nestedMap.put("key1", nestedObject1);
        nestedMap.put("key2", nestedObject2);
        testObject.setNestedMap(nestedMap);

        // Call processDecryptedFields directly
        Method processDecryptedFields = MongoDecryptionConfig.DecryptionMongoEventListener.class
                .getDeclaredMethod("processDecryptedFields", Object.class, java.util.Set.class);
        processDecryptedFields.setAccessible(true);

        when(encryptionService.decrypt("encrypted-nested-data-1")).thenReturn("nested data 1");
        when(encryptionService.decrypt("encrypted-nested-data-2")).thenReturn("nested data 2");

        // Act
        processDecryptedFields.invoke(listener, testObject, new java.util.HashSet<>());

        // Assert
        verify(encryptionService, times(2)).decrypt(anyString());
        assertEquals("nested data 1", nestedObject1.getEncryptedNestedField());
        assertEquals("nested data 2", nestedObject2.getEncryptedNestedField());
    }

    @Test
    void shouldNotDecryptNonStringFields() throws Exception {
        // Arrange
        NonStringFieldClass testObject = new NonStringFieldClass();
        testObject.setEncryptedLong(123L);

        // Call processDecryptedFields directly
        Method processDecryptedFields = MongoDecryptionConfig.DecryptionMongoEventListener.class
                .getDeclaredMethod("processDecryptedFields", Object.class, java.util.Set.class);
        processDecryptedFields.setAccessible(true);

        // Act
        processDecryptedFields.invoke(listener, testObject, new java.util.HashSet<>());

        // Assert
        verify(encryptionService, never()).decrypt(anyString());
        assertEquals(123L, testObject.getEncryptedLong());
    }

    @Test
    void shouldDecryptInheritedFields() throws Exception {
        // Arrange
        ChildClass childObject = new ChildClass();
        childObject.setEncryptedField("encrypted-parent-field");
        childObject.setChildEncryptedField("encrypted-child-field");

        // Call processDecryptedFields directly
        Method processDecryptedFields = MongoDecryptionConfig.DecryptionMongoEventListener.class
                .getDeclaredMethod("processDecryptedFields", Object.class, java.util.Set.class);
        processDecryptedFields.setAccessible(true);

        when(encryptionService.decrypt("encrypted-parent-field")).thenReturn("parent field");
        when(encryptionService.decrypt("encrypted-child-field")).thenReturn("child field");

        // Act
        processDecryptedFields.invoke(listener, childObject, new java.util.HashSet<>());

        // Assert
        verify(encryptionService, times(2)).decrypt(anyString());
        assertEquals("parent field", childObject.getEncryptedField());
        assertEquals("child field", childObject.getChildEncryptedField());
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
