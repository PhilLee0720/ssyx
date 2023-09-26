package com.lee.ssxy.activity.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lee.ssxy.activity.mapper.ActivityInfoMapper;
import com.lee.ssxy.activity.mapper.ActivityRuleMapper;
import com.lee.ssxy.activity.mapper.ActivitySkuMapper;
import com.lee.ssxy.activity.service.ActivityInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lee.ssxy.activity.service.CouponInfoService;
import com.lee.ssxy.client.product.ProductFeignClient;
import com.lee.ssxy.enums.ActivityType;
import com.lee.ssxy.model.activity.ActivityInfo;
import com.lee.ssxy.model.activity.ActivityRule;
import com.lee.ssxy.model.activity.ActivitySku;
import com.lee.ssxy.model.activity.CouponInfo;
import com.lee.ssxy.model.order.CartInfo;
import com.lee.ssxy.model.product.SkuInfo;
import com.lee.ssxy.vo.activity.ActivityRuleVo;
import com.lee.ssxy.vo.order.CartInfoVo;
import com.lee.ssxy.vo.order.OrderConfirmVo;
import org.checkerframework.checker.units.qual.C;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 活动表 服务实现类
 * </p>
 *
 * @author phil
 * @since 2023-06-22
 */
@Service
public class ActivityInfoServiceImpl extends ServiceImpl<ActivityInfoMapper, ActivityInfo> implements ActivityInfoService {

    @Resource
    private ActivityRuleMapper activityRuleMapper;
    @Resource
    private ActivitySkuMapper activitySkuMapper;

    @Resource
    private ProductFeignClient productFeignClient;

    @Resource
    private CouponInfoService couponInfoService;

    @Override
    public IPage<ActivityInfo> selectPage(Page<ActivityInfo> pageParam) {
         Page<ActivityInfo> activityInfoPage = baseMapper.selectPage(pageParam, null);
        List<ActivityInfo> records = activityInfoPage.getRecords();
        records.stream().forEach(item->{
            item.setActivityTypeString(item.getActivityType().getComment());
        });
        return activityInfoPage;
    }

    @Override
    public Map<String, Object> findActivityRuleList(Long id) {
        LambdaQueryWrapper<ActivityRule> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ActivityRule::getActivityId,id);
        List<ActivityRule> activityRules = activityRuleMapper.selectList(queryWrapper);
        HashMap<String, Object>  map = new HashMap<>();
        map.put("activityRuleList",activityRules);
        List<ActivitySku> activitySkuList = activitySkuMapper
                .selectList(new LambdaQueryWrapper<ActivitySku>().eq(ActivitySku::getActivityId, id));
        List<Long> skuIdList = activitySkuList.stream().map(ActivitySku::getSkuId).collect(Collectors.toList());
        List<SkuInfo> skuInfoList = productFeignClient.findSkuInfoList(skuIdList);
        map.put("skuInfoList",skuInfoList);
        return map;
    }

    @Override
    public void saveActivityRule(ActivityRuleVo activityRuleVo) {
        Long activityId = activityRuleVo.getActivityId();
        activityRuleMapper.delete(
                new LambdaQueryWrapper<ActivityRule>().eq(ActivityRule::getActivityId,activityId)
        );
        activitySkuMapper.delete(
                new LambdaQueryWrapper<ActivitySku>().eq(ActivitySku::getActivityId,activityId)
        );

        List<ActivityRule> activityRuleList = activityRuleVo.getActivityRuleList();
        ActivityInfo activityInfo = baseMapper.selectById(activityRuleVo.getActivityId());
        for (ActivityRule activityRule : activityRuleList) {
            activityInfo.setId(activityInfo.getId());
            activityInfo.setActivityType(activityRule.getActivityType());
            activityRuleMapper.insert(activityRule);
        }
    }

    @Override
    public List<SkuInfo> findSkuInfoByKeyword(String keyword) {
        List<SkuInfo> skuInfos = productFeignClient.findSkuInfoByKeyword(keyword);
        if(skuInfos.size() < 0){
            return skuInfos;
        }
        List<Long> skuIdList = skuInfos.stream().map(SkuInfo::getId
        ).collect(Collectors.toList());

        List<Long> existSkuIdList = baseMapper.selectSkuIdListExist(skuIdList);

        List<SkuInfo> skuInfoList = new ArrayList<>();

        for (SkuInfo skuInfo : skuInfos) {
            if(!existSkuIdList.contains(skuInfo.getId())){
                skuInfoList.add(skuInfo);
            }
        }
        return skuInfoList;

    }

    @Override
    public Map<Long, List<String>> findActivity(List<Long> skuIdList) {
        Map<Long,List<String>>  result = new HashMap<>();
        skuIdList.stream().forEach(item -> {
            List<ActivityRule> activityRuleList  = baseMapper.findActivityRule(item);
            if(!CollectionUtils.isEmpty(activityRuleList)){
                List<String> ruleList = new ArrayList<>();
                for (ActivityRule activityRule : activityRuleList) {
                    ruleList.add(this.getRuleDesc(activityRule));
                }
            }
        });
        return result;
    }

    @Override
    public Map<String, Object> findActivityAndCoupon(Long skuId, Long userId) {
        Map<String, Object> activityRuleList = this.findActivityRuleList(skuId);
        List<CouponInfo> couponInfoList = couponInfoService.findCouponInfoList(skuId,userId);
        Map<String,Object> map = new HashMap<>();
        map.put("couponInfoList",couponInfoList);
        map.putAll(activityRuleList);
        return map;
    }

    @Override
    public OrderConfirmVo findCartActivityAndCoupon(List<CartInfo> cartInfoList, Long userId) {
        List<CartInfoVo> cartInfoVoList = this.findCartActivityList(cartInfoList);
        BigDecimal activityReduceAmount = cartInfoVoList.stream().filter(cartInfoVo -> cartInfoVo != null)
                .map(cartInfoVo -> cartInfoVo.getActivityRule().getReduceAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        List<CouponInfo> couponInfoList = couponInfoService.findCartCouponInfo(cartInfoList,userId);
        BigDecimal couponReduceAmount = new BigDecimal(0);
        if(!CollectionUtils.isEmpty(cartInfoList)){
            couponInfoList.stream().filter(couponInfo -> couponInfo.getIsOptimal().intValue() == 1)
                    .map(couponInfo -> couponInfo.getAmount())
                    .reduce(BigDecimal.ZERO,BigDecimal::add);
        }
        BigDecimal originTotalAmount = cartInfoList.stream().filter(cartInfo -> cartInfo.getIsChecked() == 1)
                .map(cartInfo -> cartInfo.getCartPrice().multiply(new BigDecimal(cartInfo.getSkuNum())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalAmount = originTotalAmount.subtract(activityReduceAmount);
        OrderConfirmVo orderConfirmVo = new OrderConfirmVo();
        orderConfirmVo.setCarInfoVoList(cartInfoVoList);
        orderConfirmVo.setActivityReduceAmount(activityReduceAmount);
        orderConfirmVo.setCouponInfoList(couponInfoList);
        orderConfirmVo.setCouponReduceAmount(couponReduceAmount);
        orderConfirmVo.setOriginalTotalAmount(originTotalAmount);
        orderConfirmVo.setTotalAmount(totalAmount);
        return orderConfirmVo;
    }

    @Override
    public List<CartInfoVo> findCartActivityList(List<CartInfo> cartInfoList) {
        List<CartInfoVo> cartInfoVoList = new ArrayList<>();
        List<Long> skuIdList  = cartInfoList.stream().map(item -> item.getSkuId()).collect(Collectors.toList());
        List<ActivitySku> activitySkuList = baseMapper.selectCartActivity(skuIdList);
        Map<Long, Set<Long>> activityToSkuIdListMap = activitySkuList.stream().collect(Collectors
                .groupingBy(ActivitySku::getActivityId,
                        Collectors.mapping(ActivitySku::getSkuId
                                ,Collectors.toSet())));
        Map<Long, List<ActivityRule>> activityIdToActivityRuleListMap = new HashMap<>();
        Set<Long> activityIdSet = activitySkuList.stream().map(item -> item.getActivityId()).collect(Collectors.toSet());
        if(!CollectionUtils.isEmpty(activityIdSet)) {
            LambdaQueryWrapper<ActivityRule> queryWrapper = new LambdaQueryWrapper();
            queryWrapper.orderByDesc(ActivityRule::getConditionAmount, ActivityRule::getConditionNum);
            queryWrapper.in(ActivityRule::getActivityId, activityIdSet);
            List<ActivityRule> activityRuleList = activityRuleMapper.selectList(queryWrapper);
            activityIdToActivityRuleListMap = activityRuleList.stream().collect(Collectors.groupingBy(activityRule -> activityRule.getActivityId()));
        }
            Set<Long> activitySkuIdSet = new HashSet<>();
            if(!CollectionUtils.isEmpty(activityToSkuIdListMap)){
                Iterator<Map.Entry<Long, Set<Long>>> iterator = activityToSkuIdListMap.entrySet().iterator();
                while (iterator.hasNext()){
                    Map.Entry<Long, Set<Long>> entry = iterator.next();
                    Long activityId = entry.getKey();
                    Set<Long> currentActivitySkuIdSet = entry.getValue();
                    List<CartInfo> currentActivityCartInfoList = cartInfoList.stream()
                            .filter(cartInfo -> currentActivitySkuIdSet
                                    .contains(cartInfo.getSkuId()))
                            .collect(Collectors.toList());
                    BigDecimal activityTotalAmount = this.computeTotalAmount(currentActivityCartInfoList);
                    int activityTotalNum = this.computeCartNum(currentActivityCartInfoList);
                    List<ActivityRule> currentActivityRuleList = activityIdToActivityRuleListMap.get(activityId);
                    ActivityType activityType = currentActivityRuleList.get(0).getActivityType();
                    ActivityRule activityRule = null;
                    if(activityType == ActivityType.FULL_REDUCTION){
                         activityRule = this.computeFullReduction(activityTotalAmount, currentActivityRuleList);
                    }else {
                         activityRule = this.computeFullDiscount(activityTotalNum, activityTotalAmount, currentActivityRuleList);
                    }
                    CartInfoVo cartInfoVo = new CartInfoVo();
                    cartInfoVo.setActivityRule(activityRule);
                    cartInfoVo.setCartInfoList(currentActivityCartInfoList);
                    cartInfoVoList.add(cartInfoVo);
                    activitySkuIdSet.addAll(currentActivitySkuIdSet);
                }
            }

                skuIdList.removeAll(activitySkuIdSet);
                if(!CollectionUtils.isEmpty(skuIdList)){
                    Map<Long, CartInfo> skuIdCartInfoMap = cartInfoList.stream().collect(Collectors.toMap(CartInfo::getSkuId, cartInfo -> cartInfo));
                    for (Long skuId : skuIdList) {
                        CartInfoVo cartInfoVo = new CartInfoVo();
                        cartInfoVo.setActivityRule(null);
                        List<CartInfo> cartInfos = new ArrayList<>();
                        cartInfos.add(skuIdCartInfoMap.get(skuId));
                        cartInfoVo.setCartInfoList(cartInfos);
                        cartInfoVoList.add(cartInfoVo);
                    }
                }
                return cartInfoVoList;
    }

    private String getRuleDesc(ActivityRule activityRule) {
        ActivityType activityType = activityRule.getActivityType();
        StringBuffer ruleDesc = new StringBuffer();
        if (activityType == ActivityType.FULL_REDUCTION) {
            ruleDesc
                    .append("满")
                    .append(activityRule.getConditionAmount())
                    .append("元减")
                    .append(activityRule.getBenefitAmount())
                    .append("元");
        } else {
            ruleDesc
                    .append("满")
                    .append(activityRule.getConditionNum())
                    .append("元打")
                    .append(activityRule.getBenefitDiscount())
                    .append("折");
        }
        return ruleDesc.toString();
    }

    private ActivityRule computeFullDiscount(Integer totalNum, BigDecimal totalAmount, List<ActivityRule> activityRuleList) {
        ActivityRule optimalActivityRule = null;
        //该活动规则skuActivityRuleList数据，已经按照优惠金额从大到小排序了
        for (ActivityRule activityRule : activityRuleList) {
            //如果订单项购买个数大于等于满减件数，则优化打折
            if (totalNum.intValue() >= activityRule.getConditionNum()) {
                BigDecimal skuDiscountTotalAmount = totalAmount.multiply(activityRule.getBenefitDiscount().divide(new BigDecimal("10")));
                BigDecimal reduceAmount = totalAmount.subtract(skuDiscountTotalAmount);
                activityRule.setReduceAmount(reduceAmount);
                optimalActivityRule = activityRule;
                break;
            }
        }
        if(null == optimalActivityRule) {
            //如果没有满足条件的取最小满足条件的一项
            optimalActivityRule = activityRuleList.get(activityRuleList.size()-1);
            optimalActivityRule.setReduceAmount(new BigDecimal("0"));
            optimalActivityRule.setSelectType(1);

            StringBuffer ruleDesc = new StringBuffer()
                    .append("满")
                    .append(optimalActivityRule.getConditionNum())
                    .append("元打")
                    .append(optimalActivityRule.getBenefitDiscount())
                    .append("折，还差")
                    .append(totalNum-optimalActivityRule.getConditionNum())
                    .append("件");
            optimalActivityRule.setRuleDesc(ruleDesc.toString());
        } else {
            StringBuffer ruleDesc = new StringBuffer()
                    .append("满")
                    .append(optimalActivityRule.getConditionNum())
                    .append("元打")
                    .append(optimalActivityRule.getBenefitDiscount())
                    .append("折，已减")
                    .append(optimalActivityRule.getReduceAmount())
                    .append("元");
            optimalActivityRule.setRuleDesc(ruleDesc.toString());
            optimalActivityRule.setSelectType(2);
        }
        return optimalActivityRule;
    }

    /**
     * 计算满减最优规则
     * @param totalAmount
     * @param activityRuleList //该活动规则skuActivityRuleList数据，已经按照优惠金额从大到小排序了
     */
    private ActivityRule computeFullReduction(BigDecimal totalAmount, List<ActivityRule> activityRuleList) {
        ActivityRule optimalActivityRule = null;
        //该活动规则skuActivityRuleList数据，已经按照优惠金额从大到小排序了
        for (ActivityRule activityRule : activityRuleList) {
            //如果订单项金额大于等于满减金额，则优惠金额
            if (totalAmount.compareTo(activityRule.getConditionAmount()) > -1) {
                //优惠后减少金额
                activityRule.setReduceAmount(activityRule.getBenefitAmount());
                optimalActivityRule = activityRule;
                break;
            }
        }
        if(null == optimalActivityRule) {
            //如果没有满足条件的取最小满足条件的一项
            optimalActivityRule = activityRuleList.get(activityRuleList.size()-1);
            optimalActivityRule.setReduceAmount(new BigDecimal("0"));
            optimalActivityRule.setSelectType(1);

            StringBuffer ruleDesc = new StringBuffer()
                    .append("满")
                    .append(optimalActivityRule.getConditionAmount())
                    .append("元减")
                    .append(optimalActivityRule.getBenefitAmount())
                    .append("元，还差")
                    .append(totalAmount.subtract(optimalActivityRule.getConditionAmount()))
                    .append("元");
            optimalActivityRule.setRuleDesc(ruleDesc.toString());
        } else {
            StringBuffer ruleDesc = new StringBuffer()
                    .append("满")
                    .append(optimalActivityRule.getConditionAmount())
                    .append("元减")
                    .append(optimalActivityRule.getBenefitAmount())
                    .append("元，已减")
                    .append(optimalActivityRule.getReduceAmount())
                    .append("元");
            optimalActivityRule.setRuleDesc(ruleDesc.toString());
            optimalActivityRule.setSelectType(2);
        }
        return optimalActivityRule;
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

    private int computeCartNum(List<CartInfo> cartInfoList) {
        int total = 0;
        for (CartInfo cartInfo : cartInfoList) {
            //是否选中
            if(cartInfo.getIsChecked().intValue() == 1) {
                total += cartInfo.getSkuNum();
            }
        }
        return total;
    }
}
