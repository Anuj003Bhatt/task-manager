package com.abcorp.taskmanager.service.crypto.unidirectional;

import com.abcorp.taskmanager.config.MailConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
public class UnidirectionalCryptoServiceTest {
    @Autowired
    private UnidirectionalCryptoService cryptoService;

    @MockBean
    private MailConfig mailConfig;

    @MockBean
    private JavaMailSender mailSender;

    @Test
    public void encryptionTest() {
        String original = "test";
        String encrypted = this.cryptoService.encrypt(original);
        assertNotEquals(original, encrypted);
    }

    @Test
    public void encryptionVerificationTest() {
        String original = "test";
        String encrypted = this.cryptoService.encrypt(original);
        assertNotEquals(original, encrypted);
        assertTrue(cryptoService.verify(encrypted, original));
    }

    @Test
    public void encryptionVerificationFailTest() {
        String original = "test";
        String encrypted = this.cryptoService.encrypt(original);
        assertNotEquals(original, encrypted);
        assertFalse(cryptoService.verify(encrypted, "random"));
    }

}
