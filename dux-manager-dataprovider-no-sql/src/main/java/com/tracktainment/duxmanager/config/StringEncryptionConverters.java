package com.tracktainment.duxmanager.config;

import com.tracktainment.duxmanager.security.FieldEncryptionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

public class StringEncryptionConverters {

    @Component
    @WritingConverter
    public static class StringToEncryptedStringConverter implements Converter<String, String> {
        private final FieldEncryptionService encryptionService;

        public StringToEncryptedStringConverter(FieldEncryptionService encryptionService) {
            this.encryptionService = encryptionService;
        }

        @Override
        public String convert(String source) {
            return encryptionService.encrypt(source);
        }
    }

    @Component
    @ReadingConverter
    public static class EncryptedStringToStringConverter implements Converter<String, String> {
        private final FieldEncryptionService encryptionService;

        public EncryptedStringToStringConverter(FieldEncryptionService encryptionService) {
            this.encryptionService = encryptionService;
        }

        @Override
        public String convert(String source) {
            return encryptionService.decrypt(source);
        }
    }
}
