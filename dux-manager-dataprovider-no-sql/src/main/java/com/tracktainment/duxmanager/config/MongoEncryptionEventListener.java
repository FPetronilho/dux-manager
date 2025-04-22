package com.tracktainment.duxmanager.config;

import com.tracktainment.duxmanager.annotation.Encrypted;
import com.tracktainment.duxmanager.security.FieldEncryptionService;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterLoadEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class MongoEncryptionEventListener extends AbstractMongoEventListener<Object> {

    private final FieldEncryptionService encryptionService;

    public MongoEncryptionEventListener(FieldEncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Object> event) {
        Object source = event.getSource();
        processObject(source, true);
    }

    @Override
    public void onAfterLoad(AfterLoadEvent<Object> event) {
        Object source = event.getSource();
        processObject(source, false);
    }

    private void processObject(Object object, boolean encrypt) {
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Encrypted.class) && field.getType().equals(String.class)) {
                field.setAccessible(true);
                try {
                    String value = (String) field.get(object);
                    if (value != null) {
                        field.set(object, encrypt ?
                                encryptionService.encrypt(value) :
                                encryptionService.decrypt(value));
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Error processing encrypted field", e);
                }
            }
        }
    }
}
