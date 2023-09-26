package com.lee.ssxy.activity.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lee.ssxy.model.activity.CouponInfo;
import com.lee.ssxy.model.order.CartInfo;
import com.lee.ssxy.vo.activity.CouponRuleVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 优惠券信息 服务类
 * </p>
 *
 * @author phil
 * @since 2023-06-22
 */
public interface CouponInfoService extends IService<CouponInfo> {

    Page<CouponInfo> selecCouponInfoPage(Long page, Long limit);

    CouponInfo getCouponInfoById(Long id);

    Map<String,Object> findCouponRuleList(Long id);

    void saveCouponRule(CouponRuleVo couponRuleVo);

    List<CouponInfo> findCouponInfoList(Long skuId,Long userId);

    List<CouponInfo> findCartCouponInfo(List<CartInfo> cartInfoList, Long userId);

    CouponInfo findRangeSkuIdList(List<CartInfo> cartInfoList, Long couponId);

    void updateCouponInfoUseStatus(Long couponId, Long userId, Long orderId);
}
