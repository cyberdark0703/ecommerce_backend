package com.duc.ecommerce.exception;

public class EcommerceException extends RuntimeException{
    private ErrorCode errorCode;

    public EcommerceException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }



}
