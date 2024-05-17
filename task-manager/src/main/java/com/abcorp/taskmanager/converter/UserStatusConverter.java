package com.abcorp.taskmanager.converter;

import com.abcorp.taskmanager.type.UserStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;

@Converter
@RequiredArgsConstructor
public class UserStatusConverter implements AttributeConverter<UserStatus, Boolean> {
    @Override
    public Boolean convertToDatabaseColumn(UserStatus attribute) {
        return attribute.getIsActive();
    }

    @Override
    public UserStatus convertToEntityAttribute(Boolean dbData) {
        return dbData?UserStatus.ACTIVE:UserStatus.INACTIVE;
    }
}
