package com.liu.community.exception;

public class CustomizeException extends RuntimeException {
    String message;
    Integer code;

    @Override
    public String getMessage() {
        return message;
    }

    public Integer getCode(){
        return code;
    }

    public CustomizeException(ICustomizeErrorCode errorCode){
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }
}
