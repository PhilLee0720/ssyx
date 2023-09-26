package com.lee.ssxy.sys.controller;


import com.lee.ssxy.model.sys.Region;
import com.lee.ssxy.common.result.Result;
import com.lee.ssxy.sys.service.RegionService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 地区表 前端控制器
 * </p>
 *
 * @author phil
 * @since 2023-06-13
 */
@Api(tags = "地域接口")
@RestController
@RequestMapping("/admin/sys/region")
public class RegionController {

    @Resource
    private RegionService regionService;

    @GetMapping("/findRegionByKeyword/{Keyword}")
    public Result findRegionByKeyword(@PathVariable("keyword")String keyword){
        List<Region> list = regionService.getRegionByKeyword(keyword);
        return Result.ok(list);
    }
}

