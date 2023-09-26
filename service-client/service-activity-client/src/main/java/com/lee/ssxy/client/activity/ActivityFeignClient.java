package com.lee.ssxy.client.activity;

import com.lee.ssxy.model.activity.CouponInfo;
import com.lee.ssxy.model.order.CartInfo;
import com.lee.ssxy.vo.order.CartInfoVo;
import com.lee.ssxy.vo.order.OrderConfirmVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@FeignClient("service-activity")
public interface ActivityFeignClient {

    @PostMapping("/api/activity/inner/findActivity")
    Map<Long, List<String>> findActivity(@RequestBody List<Long> skuIdList);

    @GetMapping("api/activity/inner/findActivityAndCoupon/{skuId}/{userId}")
    Map<String,Object>  findActivityCoupon(@PathVariable("skuId") Long skuId,@PathVariable("userId")Long userId);

    @PostMapping("api/activity/inner/findCartActivityAndCoupon/{userId}")
    OrderConfirmVo findCartActivityAndCoupon(@RequestBody List<CartInfo> cartList,@PathVariable("userId") Long userId);
    @PostMapping("api/activity/inner/findRangeSkuIdList/{couponId}")
    CouponInfo findRangeSkuIdList(List<CartInfo> cartInfoList, Long couponId);

    List<CartInfoVo> findCartActivityList(List<CartInfo> cartInfoParamList);

    @GetMapping("/api/activity/inner/updateCouponInfoUseStatus/{couponId}/{userId}/{orderId}")
    public Boolean   updateCouponInfoUseStatus(@PathVariable("couponId")Long couponId,@PathVariable("userId")Long userId,@PathVariable("orderId") Long orderId);
}
