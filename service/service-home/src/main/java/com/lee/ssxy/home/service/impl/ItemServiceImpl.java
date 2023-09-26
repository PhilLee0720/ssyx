package com.lee.ssxy.home.service.impl;

import com.lee.ssxy.client.activity.ActivityFeignClient;
import com.lee.ssxy.client.product.ProductFeignClient;
import com.lee.ssxy.client.search.SkuFeignClient;
import com.lee.ssxy.home.service.ItemService;
import com.lee.ssxy.model.search.SkuEs;
import com.lee.ssxy.vo.product.SkuInfoVo;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

@Service
public class ItemServiceImpl implements ItemService {
    @Resource
    private ThreadPoolExecutor threadPoolExecutor;
    @Resource
    private ProductFeignClient productFeignClient;
    @Resource
    private ActivityFeignClient activityFeignClient;
    @Resource
    private SkuFeignClient skuFeignClient;
    @Override
    public Map<String, Object>  item(Long id, Long userId) {
        Map<String,Object> result = new HashMap<>();
        CompletableFuture<SkuInfoVo> skuInfoVo1 = CompletableFuture.supplyAsync(() -> {
            SkuInfoVo skuInfoVo = productFeignClient.getSkuInfoVo(id);
            result.put("skuInfoVo", skuInfoVo);
            return skuInfoVo;
        },threadPoolExecutor);
        CompletableFuture<Void> activityCompletableFuture = CompletableFuture.runAsync(()-> {
            Map<String, Object> activityMap = activityFeignClient.findActivityCoupon(id,userId);
            result.putAll(activityMap);
        },threadPoolExecutor);

        CompletableFuture<Void> hotCompletableFuture = CompletableFuture.runAsync(() -> {
             skuFeignClient.incrHotScore(id);
        },threadPoolExecutor);

        CompletableFuture.allOf(skuInfoVo1,activityCompletableFuture,hotCompletableFuture).join();
        System.out.println(result.toString());
        return result;
    }
}
