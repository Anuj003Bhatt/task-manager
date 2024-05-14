package com.abcorp.taskmanager.service.crypto.unidirectional;

public interface UnidirectionalCryptoService {
    String encrypt(String original);
    Boolean verify(String encrypted, String original);
}
