package com.lee.ssxy.cart.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lee.ssxy.cart.service.CartInfoService;
import com.lee.ssxy.client.product.ProductFeignClient;
import com.lee.ssxy.common.constant.RedisConst;
import com.lee.ssxy.common.exception.SsxyException;
import com.lee.ssxy.common.result.ResultCodeEnum;
import com.lee.ssxy.enums.SkuType;
import com.lee.ssxy.model.order.CartInfo;
import com.lee.ssxy.model.product.SkuInfo;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartInfoService {
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private ProductFeignClient productFeignClient;

    private String getCartKey(Long userId){
        return RedisConst.USER_KEY_PREFIX+userId.toString()+RedisConst.USER_CART_KEY_SUFFIX;
    }
    @Override
    public void addToCart(Long userId, Long skuId, Integer skuNum) {
        String cartKey = getCartKey(userId);
        BoundHashOperations hashOperations = redisTemplate.boundHashOps(cartKey);
        CartInfo cartInfo = null;
        if(hashOperations.hasKey(skuId.toString())){
             cartInfo = (CartInfo) hashOperations.get(skuId.toString());
            Integer currenSkuNum = cartInfo.getSkuNum() + skuNum;
            if(currenSkuNum < 1){
                return;
            }
            cartInfo.setSkuNum(currenSkuNum);
            cartInfo.setCurrentBuyNum(currenSkuNum);
            if(cartInfo.getPerLimit() < currenSkuNum){
                throw  new SsxyException(ResultCodeEnum.SKU_LIMIT_ERROR);
            }
            cartInfo.setIsChecked(1);
            cartInfo.setUpdateTime(new Date());
        }else{
            skuNum = 1;
            cartInfo = new CartInfo();
            SkuInfo skuInfo = productFeignClient.getSkuInfo(skuId);
            cartInfo.setSkuId(skuId);
            cartInfo.setCategoryId(skuInfo.getCategoryId());
            cartInfo.setSkuType(skuInfo.getSkuType());
            cartInfo.setIsNewPerson(skuInfo.getIsNewPerson());
            cartInfo.setUserId(userId);
            cartInfo.setCartPrice(skuInfo.getPrice());
            cartInfo.setSkuNum(skuNum);
            cartInfo.setCurrentBuyNum(skuNum);
            cartInfo.setSkuType(SkuType.COMMON.getCode());
            cartInfo.setPerLimit(skuInfo.getPerLimit());
            cartInfo.setImgUrl(skuInfo.getImgUrl());
            cartInfo.setSkuName(skuInfo.getSkuName());
            cartInfo.setWareId(skuInfo.getWareId());
            cartInfo.setIsChecked(1);
            cartInfo.setStatus(1);
            cartInfo.setCreateTime(new Date());
            cartInfo.setUpdateTime(new Date());
        }
        hashOperations.put(skuId.toString(),cartInfo);
        this.setCartKeyExpire(cartKey);
    }

    @Override
    public void deleteCart(Long skuId, Long userId) {
        String cartKey = this.getCartKey(userId);
        BoundHashOperations boundHashOperations = redisTemplate.boundHashOps(cartKey);
        if(boundHashOperations.hasKey(cartKey)){
            boundHashOperations.delete(skuId.toString());
        }
    }
    public void deleteAllCart(Long userId){
        String cartKey = this.getCartKey(userId);
        BoundHashOperations boundHashOperations = redisTemplate.boundHashOps(cartKey);
        List<CartInfo> values = boundHashOperations.values();
        for (CartInfo value : values) {
            boundHashOperations.delete(value.getSkuId().toString());
        }
    }

    @Override
    public void batchDeleteCart(List<Long> skuIdList, Long userId) {
        String cartKey = getCartKey(userId);
        BoundHashOperations boundHashOperations = redisTemplate.boundHashOps(cartKey);
        skuIdList.forEach(skuId -> {
            boundHashOperations.delete(skuId.toString());
        });

    }

    @Override
    public List<CartInfo> getCartList(Long userId) {
        List<CartInfo> cartInfoList = new ArrayList<>();
        if (StringUtils.isEmpty(userId)) {
            return cartInfoList;
        }
        String cartKey = this.getCartKey(userId);
        BoundHashOperations boundHashOperations = redisTemplate.boundHashOps(cartKey);
        List<CartInfo> values = boundHashOperations.values();
        if(!CollectionUtils.isEmpty(values)){
            values.sort(new Comparator<CartInfo>() {
                @Override
                public int compare(CartInfo o1, CartInfo o2) {
                    return o2.getCreateTime().compareTo(o1.getCreateTime());
                }
            });
        }
        return values;
    }

    @Override
    public List<CartInfo> getCartCheckedList(Long userId) {
        String cartKey = this.getCartKey(userId);
        BoundHashOperations boundHashOperations = redisTemplate.boundHashOps(cartKey);
        List<CartInfo> cartInfoList = boundHashOperations.values();
        List<CartInfo> cartInfoListNew = cartInfoList.stream().filter(cartInfo -> {
            return cartInfo.getIsChecked() == 1;
        }).collect(Collectors.toList());
        return  cartInfoListNew;
    }

    public void deleteCartChecked(Long userId){
        List<CartInfo> cartInfoList = this.getCartCheckedList(userId);
        List<Long> skuIdList = cartInfoList.stream().map(
                item -> item.getSkuId()
        ).collect(Collectors.toList());
        String cartKey = this.getCartKey(userId);
        BoundHashOperations boundHashOperations = redisTemplate.boundHashOps(cartKey);
        skuIdList.forEach(skuId->{
            boundHashOperations.delete(skuId.toString());
        });


    }

    private void setCartKeyExpire(String cartKey) {
        redisTemplate.expire(cartKey, RedisConst.USER_CART_EXPIRE, TimeUnit.SECONDS);
    }
}
