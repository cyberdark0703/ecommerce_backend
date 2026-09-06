package com.duc.ecommerce.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public enum ErrorCode {
    USER_NOT_FOUND(1,"khong tim thay user",HttpStatus.NOT_FOUND),
    INVALID_PASSWORD(2, "mat khau sai ",HttpStatus.BAD_REQUEST),
    INVALID_VALIDATION(3,"",HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_FOUND(4,"ko tim thay category tuong ung",HttpStatus.NOT_FOUND),
    CATEGORY_ALREADY_EXISTS(5,"da ton tai category day",HttpStatus.CONFLICT),
    PRODUCT_ALREADY_EXISTS(6,"da ton tai product day",HttpStatus.CONFLICT);


    final int code;
    final String message;
    final HttpStatusCode httpcode;


}
