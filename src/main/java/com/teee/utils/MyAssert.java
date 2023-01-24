package com.teee.utils;

import com.teee.project.ProjectCode;
import com.teee.vo.exception.BusinessException;

public class MyAssert {
    public static void isTrue(boolean expression, String message, Object... params) {
        if (!expression) {
            throw new BusinessException(ProjectCode.CODE_EXCEPTION_BUSSINESS, message);
        }
    }
    public static void notNull(Object obj, String message, Object... params) {
        isTrue(obj !=null, message);
    }
}
