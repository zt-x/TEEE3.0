package com.teee.project;


import com.teee.vo.exception.BusinessException;
import com.teee.vo.exception.SystemException;
import com.teee.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Xu ZhengTao
 * @version 3.0
 * 统一的异常处理机制，分为系统级异常、用户操作异常(BusinessException)、未知异常(Exception)
 */
@Slf4j
@RestControllerAdvice
public class ProjectExceptionAdvice {
    @ExceptionHandler(SystemException.class)
    public Result doSystemException(SystemException exception){
        // 记录日志'
        log.error("SystemException | Code: " + exception.getCode() + " | " + exception.getMessage());
        return new Result(exception.getCode(),null, exception.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public Result doBusinessException(BusinessException exception){
        return new Result(exception.getCode(),null, exception.getMessage());
    }


    @ExceptionHandler(Exception.class)
    public Result doException(Exception exception){
        log.error("未定义的异常 " + "| " + exception.getMessage());
        exception.printStackTrace();
        return new Result(ProjectCode.CODE_EXCEPTION,exception,"系统繁忙,请稍后重试 ...");
    }

    //补充部分
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result doHRMNSException(HttpRequestMethodNotSupportedException e){
        return new Result(ProjectCode.CODE_EXCEPTION_BUSSINESS,null, "使用了错误的请求形式");
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public Result doSQLException(DuplicateKeyException e){
        e.printStackTrace();
        return new Result(ProjectCode.CODE_EXCEPTION_BUSSINESS,null, "该数据已经被添加过啦!");
    }

    @ExceptionHandler(NumberFormatException.class)
    public Result doNumberFormatException(NumberFormatException e){
        return new Result(ProjectCode.CODE_EXCEPTION_BUSSINESS,null, "数字的格式不正确!");
    }
}
