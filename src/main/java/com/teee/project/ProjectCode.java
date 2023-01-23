package com.teee.project;


/**
 * @author Xu ZhengTao
 * @version 3.0
 *
 * code中包含了【异常返回值：<0】、【常用字符串】
 */
public class ProjectCode {
    // 返回值
    public static final int CODE_SUCCESS_NoCourse = 101;
    public static final int CODE_SUCCESS = 1;
    public static final int CODE_EXCEPTION = -1;
    public static final int CODE_EXCEPTION_SYSTEM = -2;
    public static final int CODE_EXCEPTION_BUSSINESS = -3;
    public static final int CODE_EXCEPTION_TOKENILLEGAL = -4;


    //String
    public static final String STRING_IMG_PURE_BROWN = "/img/pure_brown.png";

    //题型
    public static final Integer QueType_choice_question = 30010;
    public static final Integer QueType_fillin_question = 30011;
    public static final Integer QueType_text_question = 30012;
}
