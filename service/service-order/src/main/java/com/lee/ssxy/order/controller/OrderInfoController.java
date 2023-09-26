package com.lee.ssxy.order.controller;


import com.lee.ssxy.common.auth.AuthContextHolder;
import com.lee.ssxy.common.result.Result;
import com.lee.ssxy.model.order.OrderInfo;
import com.lee.ssxy.order.service.OrderInfoService;
import com.lee.ssxy.vo.order.OrderConfirmVo;
import com.lee.ssxy.vo.order.OrderSubmitVo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author phil
 * @since 2023-07-12
 */
@RestController
@RequestMapping("api/order/order-info")
public class OrderInfoController {
    @Resource
    private OrderInfoService orderInfoService;

    @GetMapping("auth/confirmOrder")
    public Result confirm(){
        OrderConfirmVo orderConfirmVo = orderInfoService.confirmOrder();
        return Result.ok(orderConfirmVo);
    }

    @PostMapping("auth/submitOrder")
    public Result submitOrder(@RequestBody OrderSubmitVo orderParamVo){
        Long userId = AuthContextHolder.getUserId();
        Long orderId = orderInfoService.submitOrder(orderParamVo);
        return Result.ok(orderId);
    }
    @GetMapping("auth/getOrderInfoById/{id}")
    public Result getOrderInfoById(@PathVariable("id")Long id){
        OrderInfo orderInfo = orderInfoService.getOrderInfoById(id);
        return Result.ok(orderInfo);
    }

    
}

