package com.lee.ssxy.product.api;

import com.lee.ssxy.model.product.Category;
import com.lee.ssxy.model.product.SkuInfo;
import com.lee.ssxy.product.service.CategoryService;
import com.lee.ssxy.product.service.SkuInfoService;
import com.lee.ssxy.vo.product.SkuInfoVo;
import com.lee.ssxy.vo.product.SkuStockLockVo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductInnerController {
    @Resource
    private CategoryService categoryService;
    @Resource
    private SkuInfoService skuInfoService;


    @GetMapping("inner/getCategory/{categoryId}")
    public Category getCategory(@PathVariable("categoryId") Long categoryId){
        Category category = categoryService.getById(categoryId);
        return category;
    }

    @GetMapping("inner/getSkuInfo/{skuId}")
    public SkuInfo getSkuInfo(@PathVariable("skuId")Long skuId){
        SkuInfo skuInfo = skuInfoService.getById(skuId);
        return skuInfo;
    }

    @PostMapping("inner/getSkuInfoList")
    public List<SkuInfo> getSkuInfoList(@RequestBody List<Long> skuIdList){
        List<SkuInfo> list = skuInfoService.findSkuList(skuIdList);
        return list;
    }

    @GetMapping("inner/findSkuInfoByKeyword/{keyword}")
    public List<SkuInfo> findSkuInfoByKeyword(@PathVariable("keyword")String keyword){
        List<SkuInfo> list = skuInfoService.findSkuInfoByKeyword(keyword);
        return list;
    }

    @GetMapping("inner/findCategoryList")
    List<Category> findCategoryList(@RequestBody List<Long> rangeIdList){
        List<Category> categories = categoryService.listByIds(rangeIdList);
        return categories;
    }
    @GetMapping("/inner/findAllCategoryList")
    List<Category> findAllCategoryList(){
        List<Category> allCategoryList = categoryService.findAllList();
        return allCategoryList;
    }

    @GetMapping("/inner/findNewPersonSkuInfoList")
    List<SkuInfo> findNewPersonSkuInfoList(){
        List<SkuInfo> skuInfoList = skuInfoService.findNewPersonSkuInfoList();
        return skuInfoList;
    }

    @GetMapping("/inner/getSkuInfoVo/{skuId}")
    public SkuInfoVo getSkuInfoVo(@PathVariable("skuId")Long skuId){
       SkuInfoVo skuInfoVo = skuInfoService.getSkuInfoVo(skuId);
       return skuInfoVo;
    }

    @PostMapping("/inner/checkAndLock/{orderNo}")
    public Boolean checkAndLock(@RequestBody List<SkuStockLockVo> stockLockVoList,@PathVariable String orderNo){
        return skuInfoService.checkAndLock(stockLockVoList,orderNo);
    }



}
