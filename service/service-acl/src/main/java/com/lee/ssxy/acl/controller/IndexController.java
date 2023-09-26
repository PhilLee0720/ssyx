package com.lee.ssxy.acl.controller;

import com.lee.ssxy.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Api(tags =  "登录接口")
@RequestMapping("/admin/acl/index")
public class IndexController {
    @ApiOperation("登录")
    @PostMapping("/login")
    public Result login(){
        Map<String,String> map = new HashMap<>();
        map.put("name","admin");
        return Result.ok(map);
    }
    @ApiOperation("获取信息")
    @GetMapping("/info")
    public Result getInfo(){
        Map<String,String> map = new HashMap<>();
        map.put("info","lee");
        return Result.ok(map);
    }
    @ApiOperation("退出")
    @GetMapping("/logout")
    public Result logout(){
        return Result.ok("logout");
    }
}
