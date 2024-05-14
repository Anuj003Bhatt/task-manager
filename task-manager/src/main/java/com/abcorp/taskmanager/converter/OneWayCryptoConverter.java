package com.abcorp.taskmanager.converter;

import com.abcorp.taskmanager.exception.BadRequestException;
import com.abcorp.taskmanager.service.crypto.unidirectional.UnidirectionalCryptoService;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * String encryption converter with one way converter. Generally used for password.
 * One way encoder can only encrypt and verify if encrypted string matches the original
 * but, it cannot decrypt to get back the original string.
 */
@Slf4j
@Converter
@RequiredArgsConstructor
public class OneWayCryptoConverter implements AttributeConverter<String, String> {

    private final UnidirectionalCryptoService cryptoService;

    @Override
    public String convertToDatabaseColumn(String attribute) {
        try{
            if (attribute == null) {
                return null;
            }
            return cryptoService.encrypt(attribute);
        } catch (Exception ex) {
            log.error("Unable to encrypt attribute value. Reason: {}", ex.getMessage());
            throw new BadRequestException("Unable to encrypt attribute value. Reason: %s", ex.getMessage());
        }

    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return dbData;
    }
}
