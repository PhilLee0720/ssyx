package com.lee.ssxy.home.service.impl;

import com.lee.ssxy.client.product.ProductFeignClient;
import com.lee.ssxy.client.search.SkuFeignClient;
import com.lee.ssxy.client.user.UserFeignClient;
import com.lee.ssxy.common.auth.AuthContextHolder;
import com.lee.ssxy.home.service.HomeService;
import com.lee.ssxy.model.product.Category;
import com.lee.ssxy.model.product.SkuInfo;
import com.lee.ssxy.model.search.SkuEs;
import com.lee.ssxy.vo.user.LeaderAddressVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HomeServiceImpl implements HomeService {
    @Resource
    private UserFeignClient userFeignClient;

    @Resource
    private ProductFeignClient productFeignClient;
    @Resource
    private SkuFeignClient skuFeignClient;
    @Override
    public Map<String, Object> homeData(Long userId) {
        System.out.println(AuthContextHolder.getWareId());
        Map<String,Object> result = new HashMap<>();
        //获取提货地址信息
        List<Category> allCategoryList = productFeignClient.findAllCategoryList();
        result.put("categoryList",allCategoryList);
        LeaderAddressVo leaderAddressVo = userFeignClient.getUserAddressById(userId);
        System.out.println("leaderAddressVo"+leaderAddressVo);
        result.put("leaderAddressVo",leaderAddressVo);
        List<SkuInfo> newPersonSkuInfoList = productFeignClient.findNewPersonSkuInfoList();
        result.put("newPersonSkuInfoList",newPersonSkuInfoList);
        List<SkuEs> hotSkuInfoList = skuFeignClient.findHotSkuInfoList();
        result.put("hotSkuList",hotSkuInfoList);
        return result;
    }
}
