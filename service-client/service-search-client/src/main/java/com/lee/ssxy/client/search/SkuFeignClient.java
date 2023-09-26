package com.lee.ssxy.client.search;

import com.lee.ssxy.model.search.SkuEs;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient("service-search")
public interface SkuFeignClient {


    @GetMapping("/api/search/sku/inner/findHotSkuList")
    List<SkuEs> findHotSkuInfoList();
    @GetMapping("/api/search/sku/inner/HotScore/{skuId}")
    Boolean incrHotScore(@PathVariable("skuId")Long skuId);
}
