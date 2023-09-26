package com.lee.ssxy.sys.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lee.ssxy.common.exception.SsxyException;
import com.lee.ssxy.model.sys.RegionWare;
import com.lee.ssxy.common.result.Result;
import com.lee.ssxy.sys.service.RegionWareService;
import com.lee.ssxy.vo.sys.RegionWareQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * <p>
 * 城市仓库关联表 前端控制器
 * </p>
 *
 * @author phil
 * @since 2023-06-13
 */

@Api(tags = "开通区域接口")
@RestController
@RequestMapping("/admin/sys/regionWare")
public class RegionWareController {

    @Resource
    private RegionWareService regionWareService;

    @GetMapping("/{page}/{limit}")
    @ApiOperation("开通区域分页查询")
    public Result pageList(@PathVariable("page") Long page, @PathVariable("limit") Long limit,
                           RegionWareQueryVo regionWareQueryVo) {
        Page<RegionWare> pageParam = new Page<>(page, limit);
        IPage<RegionWare> pageModel = regionWareService.queryRegionWarePage(pageParam, regionWareQueryVo);
        return Result.ok(pageModel);
    }

    @PostMapping("/save")
    @ApiOperation("添加开通区域")
    public Result save(@RequestBody RegionWare regionWare) throws SsxyException {
        boolean save = regionWareService.saveRegionWare(regionWare);
        if(!save){
            return Result.fail(null);
        }else{
            return Result.ok(null);
        }
    }

    @DeleteMapping("/remove/{id}")
    @ApiOperation("删除开通区域")
    public Result remove(@PathVariable("id")Long id){
        boolean remove = regionWareService.removeById(id);
        if(!remove){
            return Result.fail(null);
        }else{
            return Result.ok(null);
        }
    }

    @PostMapping("/updateStatus/{id}/{status}")
    public Result updateStatus(@PathVariable("id") Long id , @PathVariable("status")Integer status){
        boolean update =  regionWareService.updateStatus(id,status);
        if(!update){
            return Result.fail(null);
        }else{
            return Result.ok(null);
        }

    }
}

