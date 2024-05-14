package com.abcorp.taskmanager.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum UserStatus {
    ACTIVE(true), INACTIVE(false);

    private final Boolean isActive;

    UserStatus(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    @JsonCreator
    public UserStatus fromText(String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException(String.format("No user status available for '%s'", text));
        }
        String textLower = text.toLowerCase().trim();
        return switch (textLower) {
            case "0", "false", "inactive" -> INACTIVE;
            case "1", "true", "active" -> ACTIVE;
            default -> throw new IllegalArgumentException(String.format("No user status available for '%s'", text));
        };
    }

}
