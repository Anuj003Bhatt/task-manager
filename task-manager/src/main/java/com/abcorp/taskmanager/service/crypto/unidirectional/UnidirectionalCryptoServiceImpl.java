package com.abcorp.taskmanager.service.crypto.unidirectional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UnidirectionalCryptoServiceImpl implements UnidirectionalCryptoService {
    private final PasswordEncoder encoder;

    @Override
    public String encrypt(String original) {
        return encoder.encode(original);
    }

    @Override
    public Boolean verify(String encrypted, String original) {
        return encoder.matches(original, encrypted);
    }
}
