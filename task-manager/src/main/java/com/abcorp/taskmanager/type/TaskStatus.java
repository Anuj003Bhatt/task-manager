package com.abcorp.taskmanager.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TaskStatus {
    TODO((short)0), INPROGRESS((short)1), DONE((short)2);

    private final short status;

    @JsonValue
    public short getStatus() {
        return this.status;
    }

    TaskStatus(short status) {
        this.status = status;
    }

    @JsonCreator
    public TaskStatus fromText(String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException(String.format("No task status available for '%s'", text));
        }
        String textLower = text.toLowerCase().trim();
        return switch (textLower) {
            case "0", "todo" -> TODO;
            case "1", "inprogress" -> INPROGRESS;
            case "2", "done" -> DONE;
            default -> throw new IllegalArgumentException(String.format("No task status available for '%s'", text));
        };
    }
}
