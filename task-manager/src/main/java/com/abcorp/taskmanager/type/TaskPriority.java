package com.abcorp.taskmanager.type;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TaskPriority {
    LOW((short)0),
    MEDIUM((short)1),
    HIGH((short)2),
    CRITICAL((short)3),
    BLOCKER((short)4);

    private short priority;

    TaskPriority(short status) {
        this.priority = status;
    }

    @JsonCreator
    public TaskPriority fromText(String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException(String.format("Invalid task priority '%s'", text));
        }
        String textLower = text.toLowerCase().trim();
        return switch (textLower) {
            case "0", "low" -> LOW;
            case "1", "medium" -> MEDIUM;
            case "2", "high" -> HIGH;
            case "3", "critical" -> CRITICAL;
            case "4", "blocker" -> BLOCKER;
            default -> throw new IllegalArgumentException(String.format("Invalid task priority '%s'", text));
        };
    }
}
