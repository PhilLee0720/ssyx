package com.lee.ssxy.activity.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lee.ssxy.activity.service.ActivityInfoService;
import com.lee.ssxy.model.activity.ActivityInfo;
import com.lee.ssxy.model.product.SkuInfo;
import com.lee.ssxy.common.result.Result;
import com.lee.ssxy.vo.activity.ActivityRuleVo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 活动表 前端控制器
 * </p>
 *
 * @author phil
 * @since 2023-06-22
 */
@RestController
@RequestMapping("/admin/activity/activityInfo")
public class ActivityInfoController {
    @Resource
    private ActivityInfoService activityInfoService;

    @GetMapping("{page}/{limit}")
    public Result pageList(@PathVariable("page")Long page,@PathVariable("limit")Long limit){
        Page<ActivityInfo> pageParam = new Page<>(page,limit);
        IPage<ActivityInfo> pageModel = activityInfoService.selectPage(pageParam);
        return Result.ok(pageModel);
    }

    @PostMapping("/save")
    public Result save(@RequestBody ActivityInfo activityInfo){
        activityInfoService.save(activityInfo);
        return Result.ok(null);
    }

    @GetMapping("get/{id}")
    public Result findActivityRuleList(@PathVariable("id") Long id){
        Map<String,Object> activityRuleMap = activityInfoService.findActivityRuleList(id);
        return  Result.ok(activityRuleMap);
    }

    @PostMapping("saveActivityRule")
    public Result saveActivityRule(@RequestBody ActivityRuleVo activityRuleVo){
        activityInfoService.saveActivityRule(activityRuleVo);
        return Result.ok(null);
    }

    @GetMapping("findSkuInfoByKeyword/{keyword}")
    public Result findSkuInfoByKeyword(@PathVariable("keyword")String keyword){
       List<SkuInfo>  list = activityInfoService.findSkuInfoByKeyword(keyword);
       return Result.ok(list);
    }

}

