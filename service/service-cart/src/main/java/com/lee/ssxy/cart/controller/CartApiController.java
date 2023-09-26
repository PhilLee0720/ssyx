package com.lee.ssxy.cart.controller;


import com.lee.ssxy.cart.service.CartInfoService;
import com.lee.ssxy.client.activity.ActivityFeignClient;
import com.lee.ssxy.common.auth.AuthContextHolder;
import com.lee.ssxy.common.result.Result;
import com.lee.ssxy.model.order.CartInfo;
import com.lee.ssxy.vo.order.OrderConfirmVo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("api/cart")
public class CartApiController {
    @Resource
    private CartInfoService cartInfoService;

    @GetMapping("inner/getCartCheckedList/{userId}")
    public List<CartInfo> getCartCheckedList(@PathVariable("userId") Long userId) {
        return cartInfoService.getCartCheckedList(userId);
    }

    @Resource
    private ActivityFeignClient activityFeignClient;

    @GetMapping("addToCart/{skuId}/{skuNum}")
    public Result addToCart(@PathVariable("skuId")Long skuId,
                            @PathVariable("skuNum")Integer skuNum){
        Long userId = AuthContextHolder.getUserId();
        cartInfoService.addToCart(userId,skuId,skuNum);
        return Result.ok(null);
    }

    @DeleteMapping("deleteCart/{skuId}")
    public Result deleteCart(@PathVariable("skuId")Long skuId){
        Long userId = AuthContextHolder.getUserId();
        cartInfoService.deleteCart(skuId,userId);
        return Result.ok(null);
    }

    @DeleteMapping("deleteAllCart")
    public Result deleteAllCart(){
        Long userId = AuthContextHolder.getUserId();
        cartInfoService.deleteAllCart(userId);
        return Result.ok(null);
    }
    @PostMapping("batchDeleteCart")
    public Result batchDeleteCart(@RequestBody List<Long> skuIdList, HttpServletRequest servletRequest){
        Long userId = AuthContextHolder.getUserId();
        cartInfoService.batchDeleteCart(skuIdList,userId);
        return Result.ok(null);

    }

    @GetMapping("cartList")
    public Result cartList(){
        Long userId = AuthContextHolder.getUserId();
        List<CartInfo> cartInfoList = cartInfoService.getCartList(userId);
        return Result.ok(cartInfoList);
    }

    @GetMapping("activityCartList")
    public Result activityCartList(){
        Long userId = AuthContextHolder.getUserId();
        List<CartInfo> cartList = cartInfoService.getCartList(userId);
        OrderConfirmVo orderConfirmVo = activityFeignClient.findCartActivityAndCoupon(cartList,userId);
        return Result.ok(orderConfirmVo);
    }
}
