package com.teee.vo.exception;

import com.teee.project.ProjectCode;

public class SafeException extends RuntimeException{
    private Integer code = ProjectCode.CODE_SUCCESS;

    public SafeException() {
    }

    public SafeException(String message) {
        super(message);
    }
}
