package com.teee.controller;


import com.teee.exception.BusinessException;
import com.teee.exception.SystemException;
import com.teee.vo.R;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Xu ZhengTao
 * @version 3.0
 * 统一的异常处理机制，分为系统级异常、用户操作异常(BusinessException)、未知异常(Exception)
 */
@RestControllerAdvice
public class ProjectExceptionAdvice {
    @ExceptionHandler(SystemException.class)
    public R doSystemException(SystemException exception){
        // 记录日志
        return new R(exception.getCode(),null, exception.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public R doBusinessException(BusinessException exception){
        // 记录日志

        return new R(exception.getCode(),null, exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public R doException(Exception exception){
        return new R(Code.CODE_EXCEPTION,exception,"系统繁忙,请稍后重试 ...");
    }
}
