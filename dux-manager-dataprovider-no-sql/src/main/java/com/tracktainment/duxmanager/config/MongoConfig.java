package com.tracktainment.duxmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.Arrays;

@Configuration
public class MongoConfig {

    @Bean
    public MongoCustomConversions mongoCustomConversions(
            StringEncryptionConverters.StringToEncryptedStringConverter stringToEncryptedStringConverter,
            StringEncryptionConverters.EncryptedStringToStringConverter encryptedStringToStringConverter
    ) {

        return new MongoCustomConversions(Arrays.asList(
                stringToEncryptedStringConverter,
                encryptedStringToStringConverter
        ));
    }
}
