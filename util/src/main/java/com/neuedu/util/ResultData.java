package com.neuedu.util;

public class ResultData {
    private Integer code;
    private String message;
    private Object obj;
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Object getObj() {
        return obj;
    }
    public ResultData(){}
    private ResultData(Integer code, String message, Object obj) {
        this.code = code;
        this.message = message;
        this.obj = obj;
    }
    /**
     * 成功返回
     * @param obj 获取的数据
     * */
    public static  ResultData success(Object obj) {
        return new ResultData(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), obj);

    }
    /**
     * 成功返回
     * @param obj 获取的数据
     * @param message 返回消息
     * */
    public static ResultData success(Object obj,String message) {
        return new ResultData(ResultCode.SUCCESS.getCode(), message, obj);
    }
    /**
     * 成功失败
     * @param resultCode 失败类型
     * @param message 返回消息
     * */
    public static ResultData failed(ResultCode resultCode,String message) {
        return new ResultData(resultCode.getCode(), message, null);
    }
    /**
     * 成功失败
     * @param resultCode 失败类型
     *
     * */
    public static ResultData failed(ResultCode resultCode) {
        return new ResultData(resultCode.getCode(), resultCode.getMessage(), null);
    }
}
