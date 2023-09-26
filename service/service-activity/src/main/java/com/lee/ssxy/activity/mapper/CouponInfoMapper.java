package com.lee.ssxy.activity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lee.ssxy.model.activity.CouponInfo;
import com.lee.ssxy.model.order.CartInfo;
import feign.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 优惠券信息 Mapper 接口
 *
 * @author phil
 * @since 2023-06-22
 */
@Repository
public interface CouponInfoMapper extends BaseMapper<CouponInfo> {

    List<CouponInfo> selectCouponInfoList(@Param("skuId") Long skuId,@Param("categoryId") Long categoryId,@Param("userId") Long userId);

    List<CouponInfo> selectCartCouponInfoList(@Param("cartInfoList") List<CartInfo> cartInfoList,@Param("userId") Long userId);
}
