package com.lee.ssxy.activity.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lee.ssxy.activity.service.CouponInfoService;
import com.lee.ssxy.model.activity.CouponInfo;
import com.lee.ssxy.common.result.Result;
import com.lee.ssxy.vo.activity.CouponRuleVo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 * 优惠券信息 前端控制器
 * </p>
 *
 * @author phil
 * @since 2023-06-22
 */
@RestController
@RequestMapping("/admin/activity/couponInfo")
public class CouponInfoController {
    @Resource
    private CouponInfoService couponInfoService;
    @GetMapping("{page}/{limit}")
    public Result pageList(@PathVariable("page")Long page,@PathVariable("limit")Long limit){
        Page<CouponInfo> pageModel = couponInfoService.selecCouponInfoPage(page,limit);
        return Result.ok(pageModel);
    }

    @PostMapping("/save")
    public Result save(@RequestBody CouponInfo couponInfo){
        couponInfoService.save(couponInfo);
        return Result.ok(null);
    }

    @GetMapping("/get/{id}")
    public Result getCouponInfoById(@PathVariable("id")Long id){
        CouponInfo couponInfo = couponInfoService.getCouponInfoById(id);
        return Result.ok(couponInfo);
    }

    @GetMapping("findCouponRuleList/{id}")
    public Result findCouponRuleList(@PathVariable("id")Long id){
       Map<String,Object> list = couponInfoService.findCouponRuleList(id);
        return Result.ok(list);
    }

    @GetMapping("saveCouponRule")
    public Result saveCouponRule(@RequestBody CouponRuleVo couponRuleVo){
        couponInfoService.saveCouponRule(couponRuleVo);
        return Result.ok(null);
    }

}

