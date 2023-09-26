package com.lee.ssxy.common.result;

import lombok.Data;

@Data
public class Result<T>  {
    private Integer code;
    private String message;
    private T data;

    private Result(){

    }

    public static<T> Result<T> build(T data,ResultCodeEnum resultCodeEnum){
        Result<T> result = new Result<>();
        if(data != null){
            result.setData(data);
        }
        result.setCode(resultCodeEnum.getCode());
        result.setMessage(resultCodeEnum.getMessage());
        return  result;
    }
    public static<T> Result<T> build(T data,Integer code,String message){
        Result<T> result = new Result<>();
        if(data != null){
            result.setData(data);
        }
        result.setCode(code);
        result.setMessage(message);
        return  result;
    }

    public static<T> Result<T> ok(T data){
        Result<T> result = Result.build(data, ResultCodeEnum.SUCCESS);
        return result;
    }

    public static<T>  Result<T> fail(T data){
        Result<T> result = Result.build(data,ResultCodeEnum.FAIL);
        return result;
    }
}
