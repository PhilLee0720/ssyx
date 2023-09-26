package com.lee.ssxy.home.controller;

import com.lee.ssxy.common.auth.AuthContextHolder;
import com.lee.ssxy.home.service.HomeService;
import com.lee.ssxy.common.result.Result;
import com.lee.ssxy.vo.search.SkuEsQueryVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/home")
public class HomeApiController {

    @Resource
    private HomeService homeService;

    @GetMapping("index")
    public Result index(HttpServletRequest servletRequest){
        Long userId = AuthContextHolder.getUserId();
        Map<String,Object> map = homeService.homeData(userId);
        return Result.ok(map);
    }

}
