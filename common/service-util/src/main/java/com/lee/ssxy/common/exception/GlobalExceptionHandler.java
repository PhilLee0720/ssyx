package com.lee.ssxy.common.exception;


import com.lee.ssxy.common.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class   GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e){
        e.printStackTrace();
        return  Result.fail(null);
    }

    @ExceptionHandler(SsxyException.class)
    @ResponseBody
    public Result error(SsxyException e){
        e.printStackTrace();
        return Result.build(null,e.getCode(), e.getMessage());
    }
}
