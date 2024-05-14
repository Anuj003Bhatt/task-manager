package com.abcorp.taskmanager.service.crypto.simple;

import com.abcorp.taskmanager.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

@Slf4j
@Service
@RequiredArgsConstructor
public class SimpleCryptoServiceImpl implements SimpleCryptoService{

    @Value("${taskmanager.security.secret}")
    private String secret;

    @Value("${taskmanager.security.transformation}")
    private String transformation;

    private SecretKeySpec prepareSecreteKey() {
        MessageDigest sha = null;
        try {
            byte[] key = secret.getBytes(StandardCharsets.UTF_8);
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            return new SecretKeySpec(key, transformation);
        } catch (NoSuchAlgorithmException e) {
            log.error("Error occurred while preparing secret for simple encryption. Error: {}", e.getMessage());
            throw new BadRequestException(
                    String.format(
                            "Error occurred while preparing secret for simple encryption. Error: %s",
                            e.getMessage()
                    )
            );
        }

    }

    public String encryptWithSecret(String originalString) {
        try {
            SecretKeySpec secretKey = prepareSecreteKey();
            Cipher cipher = Cipher.getInstance(transformation);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64
                    .getEncoder()
                    .encodeToString(
                            cipher.doFinal(originalString.getBytes(StandardCharsets.UTF_8))
                    );
        } catch (Exception e) {
            log.error("Error occurred while encrypting data with simple encryption. Error: {}", e.getMessage());
            throw new BadRequestException(
                    String.format(
                            "Error occurred while encrypting data with simple encryption. Error: %s",
                            e.getMessage()
                    )
            );
        }
    }

    public String decryptWithSecret(String encryptedString) {
        try {
            SecretKeySpec secretKey = prepareSecreteKey();
            Cipher cipher = Cipher.getInstance(transformation);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(encryptedString)));
        } catch (Exception e) {
            log.error("Error occurred while decrypting data with simple encryption. Error: {}", e.getMessage());
            throw new BadRequestException(
                    String.format(
                            "Error occurred while decrypting data with simple encryption. Data: %s Error: %s",
                            encryptedString,
                            e.getMessage()
                    )
            );
        }
    }

    @Override
    public String encrypt(String original) {
        return encryptWithSecret(original);
    }

    @Override
    public String decrypt(String encrypted) {
        return decryptWithSecret(encrypted);
    }
}
