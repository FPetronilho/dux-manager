package com.tracktainment.duxmanager.security;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FieldEncryptionService {

    private final StandardPBEStringEncryptor encryptor;

    public FieldEncryptionService(@Value("${encryption.password}") String encryptionPassword) {
        encryptor = new StandardPBEStringEncryptor();
        encryptor.setAlgorithm("PBEWithHMACSHA512AndAES_256");
        encryptor.setPassword(encryptionPassword);
        encryptor.setIvGenerator(new RandomIvGenerator());
    }

    public String encrypt(String text) {
        if (text == null) {
            return null;
        }
        return encryptor.encrypt(text);
    }

    public String decrypt(String encryptedText) {
        if (encryptedText == null) {
            return null;
        }
        return encryptor.decrypt(encryptedText);
    }
}
