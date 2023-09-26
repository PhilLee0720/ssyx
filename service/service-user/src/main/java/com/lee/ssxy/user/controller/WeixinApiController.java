package com.lee.ssxy.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.lee.ssxy.common.auth.AuthContextHolder;
import com.lee.ssxy.common.constant.RedisConst;
import com.lee.ssxy.common.utils.JwtHelper;
import com.lee.ssxy.enums.UserType;
import com.lee.ssxy.common.exception.SsxyException;
import com.lee.ssxy.model.user.User;
import com.lee.ssxy.common.result.Result;
import com.lee.ssxy.common.result.ResultCodeEnum;
import com.lee.ssxy.user.service.UserService;
import com.lee.ssxy.user.utils.ConstantPropertiesUtil;
import com.lee.ssxy.user.utils.HttpClientUtils;
import com.lee.ssxy.vo.user.LeaderAddressVo;
import com.lee.ssxy.vo.user.UserLoginVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/user/weixin")
public class WeixinApiController {
    @Resource
    private UserService userService;
    @Resource
    private RedisTemplate redisTemplate;
    @ApiOperation(value = "用户授权登录")
    @GetMapping("/wxLogin/{code}")
    public Result loginWx(@PathVariable("code") String code)  {
        String wxOpenAppId = ConstantPropertiesUtil.WX_OPEN_APP_ID;
        String wxOpenAppSecret = ConstantPropertiesUtil.WX_OPEN_APP_SECRET;
        StringBuffer url = new StringBuffer().append("https://api.weixin.qq.com/sns/jscode2session").append("?appid=%s")
                .append("&secret=%s")
                .append("&js_code=%s")
                .append("&grant_type=authorization_code");
        String tokenUrl = String.format(url.toString(), wxOpenAppId, wxOpenAppSecret, code);
        String result =  null;
        try {
             result = HttpClientUtils.get(tokenUrl);
        } catch (Exception e) {
            throw new SsxyException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
        }
        JSONObject jsonObject = new JSONObject();
        JSONObject resultJson = jsonObject.parseObject(result);
        if(resultJson.getString("errcode") != null){
            throw new SsxyException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
        }
        String accessToken = resultJson.getString("session_key");
        String openid = resultJson.getString("openid");

        User user = userService.getUserByOpenId(openid);
        if (user == null){
            user = new User();
            user.setOpenId(openid);
            user.setNickName(openid);
            user.setPhotoUrl("");
            user.setUserType(UserType.USER);
            user.setIsNew(0);
            userService.save(user);
        }

        LeaderAddressVo leaderAddressVo =  userService.getAddressByLeaderId(user.getId());

        String token = JwtHelper.createToken(user.getId(), user.getNickName());
        UserLoginVo userLoginVo = userService.getUserLoginVo(user.getId());

        redisTemplate.opsForValue().set(RedisConst.USER_LOGIN_KEY_PREFIX+user.getId(),userLoginVo, RedisConst.USERKEY_TIMEOUT, TimeUnit.DAYS);
        Map<String,Object> map = new HashMap<>();
        map.put("user",user);
        map.put("token",token);
        map.put("leaderAddressVo",leaderAddressVo);
        return Result.ok(map);
    }

    @PostMapping("/auth/updateUser")
    @ApiOperation(value = "更新用户昵称与头像")
    public Result updateUser(@RequestBody User user) {
        User user1 = userService.getById(AuthContextHolder.getUserId());
        //把昵称更新为微信用户
        user1.setNickName(user.getNickName().replaceAll("[ue000-uefff]", "*"));
        user1.setPhotoUrl(user.getPhotoUrl());
        userService.updateById(user1);
        return Result.ok(null);
    }
}
