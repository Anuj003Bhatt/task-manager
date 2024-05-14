package com.abcorp.taskmanager.converter;

import com.abcorp.taskmanager.exception.BadRequestException;
import com.abcorp.taskmanager.service.crypto.simple.SimpleCryptoService;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * String encryption converter with two-way converter. Generally used for basic encryption.
 * Two-way encoder can encrypt/decrypt the original and encrypted strings
 */
@Slf4j
@Converter
@RequiredArgsConstructor
public class TwoWayCryptoConverter implements AttributeConverter<String, String> {

    private final SimpleCryptoService simpleCryptoService;

    @Override
    public String convertToDatabaseColumn(String attribute) {
        try{
            if (attribute == null) {
                return null;
            }
            return simpleCryptoService.encrypt(attribute);
        } catch (Exception ex) {
            log.error("Unable to encrypt attribute value. Reason: {}", ex.getMessage());
            throw new BadRequestException("Unable to encrypt attribute value. Reason: %s", ex.getMessage());
        }

    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        try {
            if (dbData == null) {
                return null;
            }
            return simpleCryptoService.decrypt(dbData);
        } catch (Exception ex) {
            log.error("Unable to decrypt {} attribute value. Reason: {}", dbData,ex.getMessage());
            throw new BadRequestException("Unable to decrypt attribute value. Reason: %s", ex.getMessage());
        }
    }
}
