package com.lee.ssxy.search.service.impl;

import com.lee.ssxy.client.activity.ActivityFeignClient;
import com.lee.ssxy.client.product.ProductFeignClient;
import com.lee.ssxy.common.auth.AuthContextHolder;
import com.lee.ssxy.enums.SkuType;
import com.lee.ssxy.model.product.Category;
import com.lee.ssxy.model.product.SkuInfo;
import com.lee.ssxy.model.search.SkuEs;
import com.lee.ssxy.search.repostitory.SkuRepository;
import com.lee.ssxy.search.service.SkuApiService;
import com.lee.ssxy.vo.search.SkuEsQueryVo;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SkuServiceImpl implements SkuApiService {
    @Resource
    private SkuRepository skuRepository;
    @Resource
    private ProductFeignClient productFeignClient;
    @Resource
    private RestHighLevelClient restHighLevelClient;

    @Resource
    private ActivityFeignClient activityFeignClient;

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public List<SkuEs> findHotSkuInfoList() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<SkuEs> pageModel = skuRepository.findByOrderByHotScoreDesc(pageable);
        List<SkuEs> skuEsList = pageModel.getContent();
        return skuEsList;
    }

    @Override
    public void upperSku(Long skuId) {
        System.out.println("---------------------------------"+skuId+"-----------------------------------------");
        SkuInfo skuInfo = productFeignClient.getSkuInfo(skuId);
        if(skuInfo == null){
            return;
        }
        Category category = productFeignClient.getCategory(skuInfo.getCategoryId());
        SkuEs  skuEs = new SkuEs();
        if(category != null){
            skuEs.setCategoryId(category.getId());
            skuEs.setCategoryName(category.getName());
        }
        skuEs.setId(skuInfo.getId());
        skuEs.setKeyword(skuInfo.getSkuName()+","+skuEs.getCategoryName());
        skuEs.setWareId(skuInfo.getWareId());
        skuEs.setIsNewPerson(skuInfo.getIsNewPerson());
        skuEs.setImgUrl(skuInfo.getImgUrl());
        skuEs.setTitle(skuInfo.getSkuName());
        if(skuInfo.getSkuType() == SkuType.COMMON.getCode()) {
            skuEs.setSkuType(0);
            skuEs.setPrice(skuInfo.getPrice().doubleValue());
            skuEs.setStock(skuInfo.getStock());
            skuEs.setSale(skuInfo.getSale());
            skuEs.setPerLimit(skuInfo.getPerLimit());
        } else {
            //TODO 待完善-秒杀商品

        }
        skuRepository.save(skuEs);
    }

    @Override
    public void lowerSku(Long skuId) {
        skuRepository.deleteById(skuId);
    }

    @Override
    public Page<SkuEs> search(Pageable pageable, SkuEsQueryVo skuEsQueryVo) {
        skuEsQueryVo.setWareId(AuthContextHolder.getWareId());
        Page<SkuEs> pageModel = null;
        String keyWord = skuEsQueryVo.getKeyword();
        if(StringUtils.isEmpty(keyWord)){
            pageModel = skuRepository.findByCategoryIdAndWareId(skuEsQueryVo.getCategoryId(),skuEsQueryVo.getWareId(),pageable);
            System.out.println(pageModel.getContent().toString());
        }else{
            pageModel = skuRepository.findByKeywordAndWareId(skuEsQueryVo.getKeyword(),skuEsQueryVo.getWareId(),pageable);
        }
        List<SkuEs> skuEsList = pageModel.getContent();
        if(!CollectionUtils.isEmpty(skuEsList)){
            List<Long> skuIdList   = skuEsList.stream().map(item -> item.getId()).collect(Collectors.toList());
            Map<Long,List<String>> skuIdToRuleListMap =activityFeignClient.findActivity(skuIdList);
            if(skuIdToRuleListMap != null){
                skuEsList.stream().forEach(skuEs ->  skuEs.setRuleList(skuIdToRuleListMap.get(skuEs.getId())));
            }
        }
        System.out.println(pageModel.getContent().toString());
        return pageModel;
    }

    @Override
    public void incrHotScore(Long skuId) {
        String key = "hotScore";
        Double hotScore = redisTemplate.opsForZSet().incrementScore(key,"skuId"+skuId, 1);
        if(hotScore % 10 == 0){
            Optional<SkuEs> optional = skuRepository.findById(skuId);
            SkuEs skuEs = optional.get();
            skuEs.setHotScore(Math.round(hotScore));
            skuRepository.save(skuEs);
        }
    }
}
