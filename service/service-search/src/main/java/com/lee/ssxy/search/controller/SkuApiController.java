package com.lee.ssxy.search.controller;

import com.lee.ssxy.common.auth.AuthContextHolder;
import com.lee.ssxy.model.search.SkuEs;
import com.lee.ssxy.common.result.Result;
import com.lee.ssxy.search.service.SkuApiService;
import com.lee.ssxy.vo.search.SkuEsQueryVo;
import io.swagger.models.auth.In;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("api/search/sku")
public class SkuApiController {
    @Resource
    private SkuApiService skuApiService;

    @GetMapping("inner/upperSku/{skuId}")
    public Result upperSku(@PathVariable("skuId")Long skuId){
        skuApiService.upperSku(skuId);
        return Result.ok(null);
    }

    @GetMapping("inner/lowerSku/skuId")
    public Result lowerSku(@PathVariable("skuId")Long skuId){
        skuApiService.lowerSku(skuId);
        return Result.ok(null);
    }

    @GetMapping("/inner/findHotSkuList")
    public List<SkuEs> findHotSkuInfoList(){
        return skuApiService.findHotSkuInfoList();
    }


    @GetMapping("{page}/{limit}")
    public Result listSku(@PathVariable Integer page, @PathVariable Integer limit,
                          SkuEsQueryVo skuEsQueryVo){
        System.out.println("First Intercept" + "--------------------------"+AuthContextHolder.getWareId());
        Pageable pageable = PageRequest.of(page-1,limit);
        Page<SkuEs> pageModel = skuApiService.search(pageable,skuEsQueryVo);
        System.out.println("skuEsQueryVo"+skuEsQueryVo.toString());
        return Result.ok(pageModel);
    }


    @GetMapping("inner/HotScore/{skuId}")
    public Boolean incrHotScore(@PathVariable("skuId")Long skuId){
        skuApiService.incrHotScore(skuId);
        return true;
    }



}
