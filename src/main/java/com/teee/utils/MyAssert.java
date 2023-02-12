package com.teee.utils;

import com.teee.project.ProjectCode;
import com.teee.vo.exception.BusinessException;
import com.teee.vo.exception.SafeException;

public class MyAssert {
    public static void isTrue(boolean expression, String message, Object... params) {
        if (!expression) {
            throw new BusinessException(ProjectCode.CODE_EXCEPTION_BUSSINESS, message);
        }
    }
    public static void isTrueSafe(boolean expression, String message, Object... params) {
        if (!expression) {
            throw  new SafeException(message);
        }
    }
    public static void isTrueSafe(boolean expression, Object... params) {
        if (!expression) {
            throw new SafeException();
        }
    }
    public static void notNull(Object obj, String message, Object... params) {
        isTrue(obj !=null, message);
    }
    public static void notNullSafe(Object obj, String message, Object... params){
        isTrueSafe(obj!=null,message);
    }
    public static void notNullSafe(Object obj, Object... params){
        isTrueSafe(obj!=null);
    }
}
