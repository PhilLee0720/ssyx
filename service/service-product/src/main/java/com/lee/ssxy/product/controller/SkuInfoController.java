package com.lee.ssxy.product.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lee.ssxy.model.product.SkuInfo;
import com.lee.ssxy.product.service.SkuInfoService;
import com.lee.ssxy.common.result.Result;
import com.lee.ssxy.vo.product.SkuInfoQueryVo;
import com.lee.ssxy.vo.product.SkuInfoVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * sku信息 前端控制器
 * </p>
 *
 * @author phil
 * @since 2023-06-14
 */
@RestController
@RequestMapping("admin/product/skuInfo")
public class SkuInfoController {
    @Resource
    private SkuInfoService skuInfoService;

    @ApiOperation("sku列表")
    @GetMapping("/{page}/{limit}")
    public Result pageList(@PathVariable("page") Long page, @PathVariable("limit")Long limit,
                            SkuInfoQueryVo skuInfoQueryVo){
        Page<SkuInfo> pageParam = new Page<SkuInfo>(page,limit);
        IPage<SkuInfo> pageModel = skuInfoService.selectSkuPage(pageParam,skuInfoQueryVo);
        return Result.ok(pageModel);

    }
    @ApiOperation("保存sku信息")
    @PostMapping("/save")
    public Result save(@RequestBody SkuInfoVo skuInfoVo){
       boolean save =  skuInfoService.saveSkuInfo(skuInfoVo);
       if(!save){
           return Result.fail(null);
       }else{
           return Result.ok(null);
       }
    }
    @ApiOperation("获取sku信息")
    @GetMapping("/get/{id}")
    public Result get(@PathVariable("id")Long id){
        SkuInfo skuInfo = skuInfoService.getSkuInfo(id);
        SkuInfoVo skuInfoVo = new SkuInfoVo();
        BeanUtils.copyProperties(skuInfo,skuInfoVo);
        return Result.ok(skuInfoVo);
    }

    @ApiOperation("修改Sku")
    @PutMapping("update")
    public Result update(@RequestBody SkuInfoVo skuInfoVo){
        skuInfoService.updateSkuInfo(skuInfoVo);
        return Result.ok(null);
    }
    @ApiOperation("审核sku")
    @GetMapping("check/{skuId}/{status}")
    public Result check(@PathVariable("skuId") Long skuId,@PathVariable("status") Integer status){
        skuInfoService.check(skuId,status);
        return Result.ok(null);
    }
    @ApiOperation("商品上架")
    @GetMapping("publish/{skuId}/{status}")
    public Result publish(@PathVariable("skuId") Long skuId, @PathVariable("status") Integer status){
        System.out.println("++++++++++++++"+skuId+"+++++++++++++++++++++++");
        skuInfoService.publish(skuId,status);
        return Result.ok(null);
    }
    @ApiOperation("新人专享")
    @GetMapping("isNewPerson/{skuId}/{status}")
    public Result isNewPerson(@PathVariable("skuId") Long skuId,@PathVariable("status")Integer status){
        skuInfoService.isNewPerson(skuId,status);
        return Result.ok(null);
    }

}

