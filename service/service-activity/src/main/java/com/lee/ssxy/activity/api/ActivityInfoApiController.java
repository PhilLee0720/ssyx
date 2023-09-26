package com.lee.ssxy.activity.api;

import com.lee.ssxy.activity.service.ActivityInfoService;
import com.lee.ssxy.activity.service.CouponInfoService;
import com.lee.ssxy.model.activity.CouponInfo;
import com.lee.ssxy.model.order.CartInfo;
import com.lee.ssxy.vo.order.OrderConfirmVo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/activity")
public class ActivityInfoApiController {
    @Resource
    private ActivityInfoService activityInfoService;
    @Resource
    private CouponInfoService couponInfoService;

    @PostMapping("/inner/findActivity")
    public Map<Long, List<String>> findActivity(@RequestBody List<Long> skuIdList){
        Map<Long,List<String>> findActivity = activityInfoService.findActivity(skuIdList);
        return findActivity;
    }

    @GetMapping("/inner/findActivityAndCoupon/{skuId}/{userId}")
    public Map<String,Object> findActivityAndCoupon(@PathVariable("skuId")Long skuId,@PathVariable("userId")Long userId){
        return activityInfoService.findActivityAndCoupon(skuId,userId);
    }
    @PostMapping("/inner/findCartActivityAndCoupon/{userId}")
    public OrderConfirmVo findActivityAndCoupon(@RequestBody List<CartInfo> cartInfoList,@PathVariable("userId")Long userId){
        return  activityInfoService.findCartActivityAndCoupon(cartInfoList,userId);
    }
    @PostMapping("/inner/findRangeSkuIdList/{couponId}")
    public CouponInfo findRangeSkuIdList(List<CartInfo> cartInfoList, Long couponId){
        return couponInfoService.findRangeSkuIdList(cartInfoList,couponId);
    }

    @GetMapping("/inner/updateCouponInfoUseStatus/{couponId}/{userId}/{orderId}")
    public Boolean updateCouponInfoUseStatus(@PathVariable("couponId")Long couponId,@PathVariable("userId")Long userId,@PathVariable("orderId") Long orderId){
        couponInfoService.updateCouponInfoUseStatus(couponId,userId,orderId);
        return true;
    }
}
