package com.neuedu.bean;

import com.neuedu.util.DefaultExceptionBean;
import com.neuedu.util.ResultCode;
import com.neuedu.util.ResultData;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 统一异常处理
 *
 * */
@RestControllerAdvice
public class DefaultException {
    @ExceptionHandler
    public ResultData excetionHandler(HttpServletRequest request,Exception ex){
        ex.printStackTrace();
        if(ex instanceof DefaultExceptionBean) {
            DefaultExceptionBean defaultException = (DefaultExceptionBean)ex;
            return ResultData.failed(defaultException.getResultCode(),defaultException.getMessage());
        }
        return ResultData.failed(ResultCode.FAILED);
    }
}
