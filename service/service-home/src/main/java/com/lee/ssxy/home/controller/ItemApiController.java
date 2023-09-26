package com.lee.ssxy.home.controller;

import com.lee.ssxy.common.auth.AuthContextHolder;
import com.lee.ssxy.common.result.Result;
import com.lee.ssxy.home.service.ItemService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("api/home")
public class ItemApiController {
    @Resource
    private ItemService itemService;
    @GetMapping("item/{id}")
    public Result index(@PathVariable("id") Long id){
        Long userId = AuthContextHolder.getUserId();
        Map<String,Object> map = itemService.item(id,userId);
        return Result.ok(map);
    }
}
