package com.lee.ssxy.client.product;

import com.lee.ssxy.model.product.Category;
import com.lee.ssxy.model.product.SkuInfo;
import com.lee.ssxy.vo.product.SkuInfoVo;
import com.lee.ssxy.vo.product.SkuStockLockVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


@FeignClient(value = "service-product")
public interface ProductFeignClient {

    @GetMapping("api/product/inner/getCategory/{categoryId}")
    Category getCategory(@PathVariable("categoryId") Long categoryId);

    @GetMapping("api/product/inner/getSkuInfo/{skuId}")
    SkuInfo getSkuInfo(@PathVariable("skuId")Long skuId);
    @PostMapping("api/product/inner/getSkuInfoList")
    List<SkuInfo> findSkuInfoList(@RequestBody List<Long> skuIdList);

    @GetMapping("api/product/inner/findSkuInfoByKeyword/{keyword}")
    List<SkuInfo> findSkuInfoByKeyword(String keyword);

    @GetMapping("api/product/inner/findCategoryList")
    List<Category> findCategoryList(List<Long> rangeIdList);

    @GetMapping("api/product/inner/findAllCategoryList")
    List<Category> findAllCategoryList();

    @GetMapping("api/product/inner/findNewPersonSkuInfoList")
    List<SkuInfo> findNewPersonSkuInfoList();
    @GetMapping("api/product/inner/getSkuInfoVo/{skuId}")
    SkuInfoVo getSkuInfoVo(@PathVariable("skuId")Long skuId);

    @PostMapping("/inner/checkAndLock/{orderNo}")
    Boolean checkAndLock(@RequestBody List<SkuStockLockVo> stockLockVoList, @PathVariable("orderNo") String orderNo);
}