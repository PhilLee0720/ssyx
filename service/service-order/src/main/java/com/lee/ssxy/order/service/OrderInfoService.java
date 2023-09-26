package com.lee.ssxy.order.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.lee.ssxy.model.order.OrderInfo;
import com.lee.ssxy.vo.order.OrderConfirmVo;
import com.lee.ssxy.vo.order.OrderSubmitVo;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author phil
 * @since 2023-07-12
 */
public interface OrderInfoService extends IService<OrderInfo> {

    OrderConfirmVo confirmOrder();

    Long submitOrder(OrderSubmitVo orderParamVo);

    OrderInfo getOrderInfoById(Long id);
}
