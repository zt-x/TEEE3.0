package com.teee.vo.exception;

public class SystemException extends RuntimeException{
    private Integer code;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public SystemException(String message){
        super(message);
    }
    public SystemException(String message, Throwable cause){
        super(message, cause);
        cause.printStackTrace();

    }
    public SystemException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public SystemException(Integer code,String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        cause.printStackTrace();

    }

}
