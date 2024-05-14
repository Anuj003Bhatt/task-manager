package com.abcorp.taskmanager.service.crypto.simple;

public interface SimpleCryptoService {
    String encrypt(String original);
    String decrypt(String encrypted);
}
