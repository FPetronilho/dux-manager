package com.tracktainment.duxmanager.config;

import com.tracktainment.duxmanager.annotation.Encrypted;
import com.tracktainment.duxmanager.encryption.EncryptionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class MongoEncryptionConfig {

    private final EncryptionService encryptionService;

    public MongoEncryptionConfig(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    @Bean
    public EncryptionMongoEventListener encryptionMongoEventListener() {
        return new EncryptionMongoEventListener();
    }

    public class EncryptionMongoEventListener extends AbstractMongoEventListener<Object> {

        @Override
        public void onBeforeConvert(BeforeConvertEvent<Object> event) {
            Object source = event.getSource();
            if (source != null) {
                processEncryptedFields(source, new HashSet<>());
            }
        }

        private void processEncryptedFields(Object object, Set<Object> processedObjects) {
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
                            String encryptedValue = encryptionService.encrypt((String) value);
                            field.set(object, encryptedValue);
                        }
                    } else {
                        Object fieldValue = field.get(object);
                        if (fieldValue != null && !isBasicType(fieldValue.getClass())) {
                            processEncryptedFields(fieldValue, processedObjects);
                        }
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Error accessing field for encryption", e);
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
