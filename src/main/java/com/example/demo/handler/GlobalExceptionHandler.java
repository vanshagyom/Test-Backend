package com.example.demo.handler;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.utils.ResponseHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // WHEN ANY VALIDATION FAILS.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> errorMessages = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        return ResponseHandler.generateResponse(
                "Validation Failed",
                HttpStatus.BAD_REQUEST,
                errorMessages,
                request
        );
    }

    //CUSTOM ERROR HANDLING USE THERE WHERE WE WANT TO RETURN NOT FOUND
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request){
        return ResponseHandler.generateResponse(
                ex.getMessage(),
                HttpStatus.NOT_FOUND,
                null,
                request
        );
    }

    //FOR EXCEPTION LIKE WHEN INVALID ARGUMENTS ARE PASSED LIKE NEGATIVE SALARY
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest  request){
        return ResponseHandler.generateResponse(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST,
                null,
                request
        );
    }

    //FOR WHEN WE TRIGGER DB CONSTRAINTS THAT VIOLATE DB RULES LIKE UNIQUE KEYS
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleDbConflict(DataIntegrityViolationException ex, HttpServletRequest request){
        String message = ex.getRootCause() != null ? ex.getRootCause().getMessage() : ex.getMessage();
        return ResponseHandler.generateResponse(
                "DB Constraint Violation" + message,
                HttpStatus.CONFLICT,
                null,
                request
        );
    }

    //FOR WHEN WE SEND MALFORMED JSON
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleInvalidJson(HttpMessageNotReadableException ex, HttpServletRequest request){
        return ResponseHandler.generateResponse(
                "Malformed JSON Request",
                HttpStatus.BAD_REQUEST,
                null,
                request
        );
    }

    //FOR WHEN THERE IS A MISSING PARAMETER
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleMissingParams(MissingServletRequestParameterException ex, HttpServletRequest request){
        return ResponseHandler.generateResponse(
                "Missing Parameter: " + ex.getParameterName(),
                HttpStatus.BAD_REQUEST,
                null,
                request
        );
    }

    //WHEN WRONG TYPE OF REQUEST IS SENT
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ResponseEntity<Object> handleWrongMethod(HttpRequestMethodNotSupportedException ex, HttpServletRequest request){
        return ResponseHandler.generateResponse(
                "Method nopt allowed: " + ex.getMethod(),
                HttpStatus.METHOD_NOT_ALLOWED,
                null,
                request
        );
    }

    //WHEN WRONG URL IS VISITED
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleNotFoundUrl (NoHandlerFoundException ex, HttpServletRequest request){
        return ResponseHandler.generateResponse(
                "Not handler for: " + ex.getRequestURL(),
                HttpStatus.NOT_FOUND,
                null,
                request
        );
    }

    // FOR ANY OTHER KIND OF EXCEPTION
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleGeneralExceptions(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
