package com.abcorp.taskmanager.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
@Getter
@Setter
public class NotFoundException extends RuntimeException {
    private String message;

    public NotFoundException() {
        super();
    }

    /**
     * The message passed in this constructor is directly set as the exception message.
     *
     * @param message Exception message
     */
    public NotFoundException(String message) {
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
    public NotFoundException(String message, Object...args) {
        super(message.formatted(args));
        this.message = message.formatted(args);
    }
}
