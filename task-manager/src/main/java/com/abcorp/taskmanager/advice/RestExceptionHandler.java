package com.abcorp.taskmanager.advice;

import com.abcorp.taskmanager.exception.BadRequestException;
import com.abcorp.taskmanager.exception.NotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.Map;

/**
 * This controller advice tackles all the custom exceptions in the code and the lombok object validations.
 */
@ControllerAdvice
public class RestExceptionHandler {

    /**
     * This is the handler for the custom BadRequestException and the custom NotFoundException
     *
     * @param ex The BadRequest or the NotFound exception
     * @return Response with proper error message
     * @see BadRequestException
     * @see NotFoundException
     */
    @ExceptionHandler(value = {BadRequestException.class, NotFoundException.class})
    protected ResponseEntity<Object> handleException(RuntimeException ex) {
        return new ResponseEntity<>(
                Map.of("error",ex.getMessage()),
                new HttpHeaders(),
                (ex instanceof BadRequestException)?HttpStatus.BAD_REQUEST:HttpStatus.NOT_FOUND
        );
    }

    /**
     * This is the handler for the lombok validation errors.
     *
     * @param ex MethodArgumentNotValidException for invalid data in payload.
     * @return Response with proper error message
     */
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    protected ResponseEntity<Object> handleException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getMessage();
        List<ObjectError> errors = ex.getBindingResult().getAllErrors();
        if (!errors.isEmpty()) {
            errorMessage = errors.get(0).getDefaultMessage();
        }
        return new ResponseEntity<>(
                Map.of("error", errorMessage),
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST
        );
    }
}
