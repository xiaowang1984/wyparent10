package com.neuedu.util;

/**
 * 自定义异常
 *
 *
 * */
public class DefaultExceptionBean extends Exception {
    private static final long serialVersionUID = 4438639445320085395L;
    private ResultCode resultCode;

    public DefaultExceptionBean(ResultCode resultCode, String message) {
        super(message);
        this.resultCode = resultCode;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }
}
