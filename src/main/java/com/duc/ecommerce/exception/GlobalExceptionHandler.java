package com.duc.ecommerce.exception;

import com.duc.ecommerce.dto.response.ExceptionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler (exception = EcommerceException.class)
    ResponseEntity<ExceptionResponse> handlingEcommerceException (EcommerceException exception){
        ErrorCode errorCode = exception.getErrorCode();
        ExceptionResponse response = new ExceptionResponse();
        response.setCode(errorCode.getCode());
        response.setMessage(errorCode.getMessage());
        return ResponseEntity.status(errorCode.getHttpcode()).body(response);
    }

    @ExceptionHandler (exception = MethodArgumentNotValidException.class)
    ResponseEntity<ExceptionResponse> handlingValidation (MethodArgumentNotValidException exception){
        ErrorCode errorCode = ErrorCode.INVALID_VALIDATION;
        ExceptionResponse response = new ExceptionResponse();

        String message = exception.getBindingResult()
                        .getFieldError()
                .getDefaultMessage();

        response.setCode(errorCode.getCode());
        response.setMessage(message);
        return ResponseEntity.status(errorCode.getHttpcode()).body(response);

    }
}
