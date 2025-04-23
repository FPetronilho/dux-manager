package com.tracktainment.duxmanager.config;

import com.tracktainment.duxmanager.annotation.Encrypted;
import com.tracktainment.duxmanager.encryption.EncryptionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterConvertEvent;

import java.lang.reflect.Field;
import java.util.HashSet;
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

            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);

                try {
                    if (field.isAnnotationPresent(Encrypted.class)) {
                        Object value = field.get(object);

                        if (value instanceof String) {
                            String decryptedValue = encryptionService.decrypt((String) value);
                            field.set(object, decryptedValue);
                        }
                    } else {
                        Object fieldValue = field.get(object);
                        if (fieldValue != null && !isBasicType(fieldValue.getClass())) {
                            processDecryptedFields(fieldValue, processedObjects);
                        }
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Error accessing field for decryption", e);
                }
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