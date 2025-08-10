package com.e_commerce.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> response = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(err -> {
            String field = ((FieldError)err).getField();
            String message = err.getDefaultMessage();
            response.put(field, message);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIResponseException> resourceNotFoundException(ResourceNotFoundException e) {
//        String message = e.getMessage();
        APIResponseException apiResponseException = new APIResponseException(e.getMessage(), false);
        return new ResponseEntity<>(apiResponseException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(APIException.class)
    public ResponseEntity<APIResponseException> apiException(APIException e) {
//        String message = e.getMessage();
        APIResponseException apiResponseException = new APIResponseException(e.getMessage(), false);
        return new ResponseEntity<>(apiResponseException, HttpStatus.BAD_REQUEST);
    }
}
