package com.lee.ssxy.common.auth;

import com.lee.ssxy.vo.user.UserLoginVo;
import java.lang.ThreadLocal;
public class AuthContextHolder {
    private static ThreadLocal<Long> userId = new ThreadLocal<>();
    private static ThreadLocal<Long> wareId = new ThreadLocal<>();

    private static ThreadLocal<UserLoginVo> userLoginVo = new ThreadLocal<>();

    public static void setUserId(Long _userId){
        userId.set(_userId);
    }

    public static Long getUserId(){
        return userId.get();
    }

    public static void setWareId(Long _wareId){
        wareId.set(_wareId);
    }

    public static Long getWareId(){
        return wareId.get();
    }

    public static void setUserLoginVo(UserLoginVo _userLoginVo){
        userLoginVo.set(_userLoginVo);
    }
    public static  UserLoginVo getUserLoginVo(){
       return userLoginVo.get();
    }
}
