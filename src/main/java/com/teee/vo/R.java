package com.teee.vo;

/**
 * @author Xu ZhengTao
 * @version 3.0
 * 该类是controller返回给前端的数据格式
 * code为错误码
 * data为携带数据
 * msg为消息
 */
public class R {
    private int code;
    private Object data;
    private String msg;

    public R(int code, Object data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public R(int code, Object data) {
        this.code = code;
        this.data = data;
    }

    public R(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
