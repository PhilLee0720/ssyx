package com.lee.ssxy.activity.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lee.ssxy.activity.mapper.CouponInfoMapper;
import com.lee.ssxy.activity.mapper.CouponRangeMapper;
import com.lee.ssxy.activity.mapper.CouponUseMapper;
import com.lee.ssxy.activity.service.CouponInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lee.ssxy.client.product.ProductFeignClient;
import com.lee.ssxy.enums.CouponRangeType;
import com.lee.ssxy.enums.CouponStatus;
import com.lee.ssxy.model.activity.CouponInfo;
import com.lee.ssxy.model.activity.CouponRange;
import com.lee.ssxy.model.activity.CouponUse;
import com.lee.ssxy.model.order.CartInfo;
import com.lee.ssxy.model.product.Category;
import com.lee.ssxy.model.product.SkuInfo;
import com.lee.ssxy.vo.activity.CouponRuleVo;
import jodd.util.CollectionUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 优惠券信息 服务实现类
 * </p>
 *
 * @author phil
 * @since 2023-06-22
 */
@Service
public class CouponInfoServiceImpl extends ServiceImpl<CouponInfoMapper, CouponInfo> implements CouponInfoService {

    @Resource
    private CouponRangeMapper couponRangeMapper;

    @Resource
    private ProductFeignClient productFeignClient;

    @Resource
    private CouponUseMapper couponUseMapper;

    @Override
    public Page<CouponInfo> selecCouponInfoPage(Long page, Long limit) {
        Page<CouponInfo> pageParam = new Page<>(page,limit);
        Page<CouponInfo> couponInfoPage = baseMapper.selectPage(pageParam, null);
        List<CouponInfo> records = couponInfoPage.getRecords();
        records.stream().forEach(item->{
            item.setCouponTypeString(item.getCouponType().getComment());
            CouponRangeType rangeType = item.getRangeType();
            if(rangeType != null){
                item.setRangeTypeString(item.getRangeType().getComment());
            }
        });
        return couponInfoPage;
    }

    @Override
    public CouponInfo getCouponInfoById(Long id) {
        CouponInfo couponInfo = baseMapper.selectById(id);
        couponInfo.setCouponTypeString(couponInfo.getCouponType().getComment());
        if(couponInfo.getRangeType() != null) {
            couponInfo.setRangeTypeString(couponInfo.getRangeType().getComment());
        }
        return couponInfo;
    }

    @Override
    public Map<String,Object> findCouponRuleList(Long id) {
        Map<String,Object> result = new HashMap<>();
        CouponInfo couponInfo = baseMapper.selectById(id);
        List<CouponRange> couponRangeList = couponRangeMapper.selectList(new LambdaQueryWrapper<CouponRange>().eq(CouponRange::getCouponId, id));
        List<Long> rangeIdList =  couponRangeList.stream().map(CouponRange::getId).collect(Collectors.toList());
        if(!CollectionUtils.isEmpty(rangeIdList)){
            if(couponInfo.getRangeType() == CouponRangeType.SKU){
                List<SkuInfo> skuInfoList = productFeignClient.findSkuInfoList(rangeIdList);
                result.put("skuInfoList",skuInfoList);
            }else if(couponInfo.getRangeType() == CouponRangeType.CATEGORY){
                List<Category> categoryList = productFeignClient.findCategoryList(rangeIdList);
                result.put("categoryList",categoryList);
            }
        }
        return result;
    }

    @Override
    public void saveCouponRule(CouponRuleVo couponRuleVo) {
        couponRangeMapper
                .delete(
                        new LambdaQueryWrapper<CouponRange>()
                                .eq(CouponRange::getCouponId,couponRuleVo
                                        .getCouponId()));
        CouponInfo couponInfo = this.getById(couponRuleVo.getCouponId());
        // couponInfo.setCouponType();
        couponInfo.setRangeType(couponRuleVo.getRangeType());
        couponInfo.setConditionAmount(couponRuleVo.getConditionAmount());
        couponInfo.setAmount(couponRuleVo.getAmount());
        couponInfo.setConditionAmount(couponRuleVo.getConditionAmount());
        couponInfo.setRangeDesc(couponRuleVo.getRangeDesc());

        baseMapper.updateById(couponInfo);
        List<CouponRange> couponRangeList = couponRuleVo.getCouponRangeList();
        for (CouponRange couponRange : couponRangeList) {
            couponRange.setCouponId(couponRuleVo.getCouponId());
            couponRangeMapper.insert(couponRange);
        }
    }

    @Override
    public List<CouponInfo> findCouponInfoList(Long skuId,Long userId) {
        SkuInfo skuInfo = productFeignClient.getSkuInfo(skuId);
        List<CouponInfo> couponInfoList = baseMapper.selectCouponInfoList(skuInfo.getId(),skuInfo.getCategoryId(),userId);
        return couponInfoList;
    }

    @Override
    public List<CouponInfo> findCartCouponInfo(List<CartInfo> cartInfoList, Long userId) {
        List<CouponInfo> userAllCouPonInfoList = baseMapper.selectCartCouponInfoList(cartInfoList,userId);
        List<Long> couponIdList = userAllCouPonInfoList.stream().map(couponInfo -> couponInfo.getId()).collect(Collectors.toList());
        LambdaQueryWrapper<CouponRange> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(CouponRange::getId,couponIdList);
        List<CouponRange> couponRangeList = couponRangeMapper.selectList(queryWrapper);
        Map<Long,List<Long>> couponIdToSkuIdMap = this.findCouponInfoToSkuIdMap(cartInfoList,couponRangeList);
        BigDecimal reduceAmount = new BigDecimal(0);
        CouponInfo optimalCouponInfo = null;
        for (CouponInfo couponInfo : userAllCouPonInfoList) {
            if(couponInfo.getRangeType() == CouponRangeType.ALL){
                BigDecimal totalAmount = computeTotalAmount(cartInfoList);
                if(totalAmount.subtract(couponInfo.getConditionAmount()).doubleValue() >= 0){
                    couponInfo.setIsSelect(1);
                }
            }else{
                List<Long> skuIdList = couponIdToSkuIdMap.get(couponInfo.getId());
                List<CartInfo> currentCartInfoList = cartInfoList.stream().filter(cartInfo -> skuIdList.contains(cartInfo.getSkuId()))
                        .collect(Collectors.toList());
                BigDecimal totalAmount = computeTotalAmount(currentCartInfoList);
                if(totalAmount.subtract(couponInfo.getConditionAmount()).doubleValue()>=0){
                    couponInfo.setIsSelect(1);
                }
            }
            if (couponInfo.getIsSelect().intValue() == 1 && couponInfo.getAmount().subtract(reduceAmount).doubleValue() > 0) {
                reduceAmount = couponInfo.getAmount();
                optimalCouponInfo = couponInfo;
            }
        }
        if(optimalCouponInfo != null){
            optimalCouponInfo.setIsOptimal(1);
        }
        return userAllCouPonInfoList;
    }

    @Override
    public CouponInfo findRangeSkuIdList(List<CartInfo> cartInfoList, Long couponId) {
        CouponInfo couponInfo = baseMapper.selectById(couponId);
        if(couponInfo == null){
            return null;
        }
        List<CouponRange> couponRangeList = couponRangeMapper.selectList(
                new LambdaQueryWrapper<CouponRange>()
                        .eq(CouponRange::getCouponId, couponId)
        );
        Map<Long, List<Long>> couponInfoToSkuIdMap = this.findCouponInfoToSkuIdMap(cartInfoList,couponRangeList);
        List<Long> skuIdList = couponInfoToSkuIdMap.entrySet().iterator().next().getValue();
        couponInfo.setSkuIdList(skuIdList);
        return couponInfo;
    }

    @Override
    public void updateCouponInfoUseStatus(Long couponId, Long userId, Long orderId) {
        CouponUse couponUse = couponUseMapper.selectOne(new LambdaQueryWrapper<CouponUse>()
                .eq(CouponUse::getCouponId, couponId)
                .eq(CouponUse::getUserId, userId)
                .eq(CouponUse::getOrderId, orderId));
        couponUse.setCouponStatus(CouponStatus.USED);
        couponUseMapper.updateById(couponUse);
    }

    private BigDecimal computeTotalAmount(List<CartInfo> cartInfoList) {
        BigDecimal total = new BigDecimal("0");
        for (CartInfo cartInfo : cartInfoList) {
            //是否选中
            if(cartInfo.getIsChecked().intValue() == 1) {
                BigDecimal itemTotal = cartInfo.getCartPrice().multiply(new BigDecimal(cartInfo.getSkuNum()));
                total = total.add(itemTotal);
            }
        }
        return total;
    }

    private Map<Long, List<Long>> findCouponInfoToSkuIdMap(List<CartInfo> cartInfoList, List<CouponRange> couponRangeList) {
        Map<Long,List<Long>> couponIdToSkuIdMap = new HashMap<>();
        Map<Long, List<CouponRange>> couponRangeToRangeListMap = couponRangeList.stream().collect(Collectors.groupingBy(couponRange -> couponRange.getCouponId()));
        Iterator<Map.Entry<Long, List<CouponRange>>> iterator = couponRangeToRangeListMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<Long, List<CouponRange>> entry = iterator.next();
            Long couponId = entry.getKey();
            List<CouponRange> rangeList = entry.getValue();
            Set<Long> skuIdSet = new HashSet<>();
            for (CartInfo cartInfo : cartInfoList) {
                for (CouponRange couponRange : rangeList) {
                    if(couponRange.getRangeType() == CouponRangeType.SKU && couponRange.getRangeId().longValue() == cartInfo.getSkuId()){
                        skuIdSet.add(cartInfo.getSkuId());
                    }else if(couponRange.getRangeType() == CouponRangeType.CATEGORY && couponRange.getRangeId().longValue() == cartInfo.getSkuId()){
                        skuIdSet.add(cartInfo.getSkuId());
                    }else{

                    }
                }
            }
            couponIdToSkuIdMap.put(couponId,new ArrayList<>(skuIdSet));
        }
        return couponIdToSkuIdMap;
    }
}
