package com.abcorp.taskmanager.service.crypto.simple;

import com.abcorp.taskmanager.config.MailConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
public class SimpleCryptoServiceTest {
    @Autowired
    private SimpleCryptoService simpleCryptoService;

    @MockBean
    private MailConfig mailConfig;

    @MockBean
    private JavaMailSender mailSender;


    @Test
    public void encryptionTest() {
        String original = "testString";
        String encrypted = this.simpleCryptoService.encrypt(original);
        assertNotEquals(original, encrypted);
    }

    @Test
    public void decryptionTest() {
        String original = "testString";
        String encrypted = this.simpleCryptoService.encrypt(original);
        String decrypted = this.simpleCryptoService.decrypt(encrypted);
        assertEquals(decrypted, original);
    }
}
