package com.sau.swe.utils.exception;

import com.sau.swe.utils.response.GenericResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class RestExceptionHandler {

    @ExceptionHandler(value = GenericFinanceException.class)
    public ResponseEntity<GenericResponse<Void>> handleGenericFinanceException(GenericFinanceException exception){
        GenericResponse<Void> response = GenericResponse.error(exception.getMessage());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }
}
