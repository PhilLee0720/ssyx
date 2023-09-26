package com.lee.ssxy.common.exception;

import com.lee.ssxy.common.result.ResultCodeEnum;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SsxyException extends RuntimeException {

    private Integer code;
    private String message;

    public SsxyException(String message,Integer code){
        super(message);
        this.code  = code;
    }

    public SsxyException(ResultCodeEnum resultCodeEnum){
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }
}
