package com.duc.ecommerce.exception;

import com.duc.ecommerce.dto.response.ExceptionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;



@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    ResponseEntity<ExceptionResponse> handlingUnwantedException(Exception exception) {
        log.error("Unexpected error occurred", exception);

        ExceptionResponse response = new ExceptionResponse();
        response.setCode(9999);
        response.setMessage("Unwanted error");

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }


    @ExceptionHandler (exception = EcommerceException.class)
    ResponseEntity<ExceptionResponse> handlingEcommerceException (EcommerceException exception){
        ErrorCode errorCode = exception.getErrorCode();

        log.error("ERROR CODE = {}",errorCode);
        log.error("HTTP CODE = {}",errorCode.getHttpcode());

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
                .getFieldError() != null
                ? exception.getBindingResult()
                .getFieldError()
                .getDefaultMessage()
                : errorCode.getMessage();

        log.warn("Validation failed: {}", message);

        response.setCode(errorCode.getCode());
        response.setMessage(message);
        return ResponseEntity.status(errorCode.getHttpcode()).body(response);

    }
}
