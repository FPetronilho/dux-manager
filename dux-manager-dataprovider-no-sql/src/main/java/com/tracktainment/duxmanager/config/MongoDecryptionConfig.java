package com.tracktainment.duxmanager.config;

import com.tracktainment.duxmanager.annotation.Encrypted;
import com.tracktainment.duxmanager.encryption.EncryptionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterConvertEvent;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Configuration
public class MongoDecryptionConfig {

    private final EncryptionService encryptionService;

    public MongoDecryptionConfig(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    @Bean
    public DecryptionMongoEventListener decryptionMongoEventListener() {
        return new DecryptionMongoEventListener();
    }

    public class DecryptionMongoEventListener extends AbstractMongoEventListener<Object> {

        @Override
        public void onAfterConvert(AfterConvertEvent<Object> event) {
            Object source = event.getSource();
            if (source != null) {
                processDecryptedFields(source, new HashSet<>());
            }
        }

        private void processDecryptedFields(Object object, Set<Object> processedObjects) {
            if (object == null || processedObjects.contains(object)) {
                return;
            }

            processedObjects.add(object);
            Class<?> clazz = object.getClass();

            if (isBasicType(clazz) || clazz.isEnum() || clazz.isArray()) {
                return;
            }

            // Process fields in current class and all superclasses
            Class<?> currentClass = clazz;
            while (currentClass != null && currentClass != Object.class) {
                for (Field field : currentClass.getDeclaredFields()) {
                    field.setAccessible(true);

                    try {
                        Object fieldValue = field.get(object);
                        if (fieldValue == null) {
                            continue;
                        }

                        if (field.isAnnotationPresent(Encrypted.class) && fieldValue instanceof String) {
                            // Decrypt string fields only
                            String decryptedValue = encryptionService.decrypt((String) fieldValue);
                            field.set(object, decryptedValue);
                        } else {
                            // Process nested objects
                            if (fieldValue instanceof Collection) {
                                for (Object item : (Collection<?>) fieldValue) {
                                    if (item != null && !isBasicType(item.getClass())) {
                                        processDecryptedFields(item, processedObjects);
                                    }
                                }
                            } else if (fieldValue instanceof Map) {
                                for (Object value : ((Map<?, ?>) fieldValue).values()) {
                                    if (value != null && !isBasicType(value.getClass())) {
                                        processDecryptedFields(value, processedObjects);
                                    }
                                }
                            } else if (!isBasicType(fieldValue.getClass())) {
                                processDecryptedFields(fieldValue, processedObjects);
                            }
                        }
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Error accessing field for decryption", e);
                    }
                }
                currentClass = currentClass.getSuperclass();
            }
        }

        private boolean isBasicType(Class<?> clazz) {
            return clazz.isPrimitive() ||
                    clazz == String.class ||
                    Number.class.isAssignableFrom(clazz) ||
                    Boolean.class == clazz ||
                    Character.class == clazz ||
                    clazz.getPackage() != null &&
                            (clazz.getPackage().getName().startsWith("java.") ||
                                    clazz.getPackage().getName().startsWith("javax.") ||
                                    clazz.getPackage().getName().startsWith("org.springframework."));
        }
    }
}
