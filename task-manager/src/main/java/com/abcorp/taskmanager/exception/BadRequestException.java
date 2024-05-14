package com.abcorp.taskmanager.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

/**
 * This is a custom exception representing a Bad Request.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
@Getter
@Setter
public class BadRequestException extends RuntimeException {
    private String message;

    public BadRequestException() {
        super();
    }

    /**
     * The message passed in this constructor is directly set as the exception message.
     *
     * @param message Exception message
     */
    public BadRequestException(String message) {
        super(message);
        this.message = message;
    }

    /**
     * The message passed in this constructor is formatted first using the arguments passed.
     * <br><br>
     * For Example, consider below line of code:<br>
     * BadRequestException("The error is %s and reason is %s", "A", "B") <br><br>
     *
     * This will result in the below message being set out:<br>
     * "The error is A and reason is B"
     *
     * @param message Exception message
     */
    public BadRequestException(String message, Object...args) {
        super(message.formatted(args));
        this.message = message.formatted(args);
    }
}