package com.lee.ssxy.order.service.impl;

import com.lee.ssxy.model.order.OrderItem;
import com.lee.ssxy.order.mapper.OrderItemMapper;
import com.lee.ssxy.order.service.OrderItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单项信息 服务实现类
 * </p>
 *
 * @author phil
 * @since 2023-07-12
 */
@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements OrderItemService {

}
