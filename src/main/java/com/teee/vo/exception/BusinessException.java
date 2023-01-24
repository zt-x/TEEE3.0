package com.teee.vo.exception;

import com.teee.project.ProjectCode;

public class BusinessException extends RuntimeException {
    private Integer code = ProjectCode.CODE_EXCEPTION_BUSSINESS;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public BusinessException(String message){
        super(message);
    }
    public BusinessException(String message, Throwable cause){
        super(message, cause);
    }
    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(Integer code,String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        cause.printStackTrace();
    }
}
