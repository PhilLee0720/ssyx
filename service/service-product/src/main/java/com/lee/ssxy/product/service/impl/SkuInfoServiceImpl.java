package com.lee.ssxy.product.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lee.ssxy.common.constant.RedisConst;
import com.lee.ssxy.common.exception.SsxyException;
import com.lee.ssxy.common.mq.constant.MqConst;
import com.lee.ssxy.common.mq.service.RabbitService;
import com.lee.ssxy.common.result.ResultCodeEnum;
import com.lee.ssxy.model.product.SkuAttrValue;
import com.lee.ssxy.model.product.SkuImage;
import com.lee.ssxy.model.product.SkuInfo;
import com.lee.ssxy.model.product.SkuPoster;
import com.lee.ssxy.product.mapper.SkuInfoMapper;
import com.lee.ssxy.product.service.SkuAttrValueService;
import com.lee.ssxy.product.service.SkuImageService;
import com.lee.ssxy.product.service.SkuInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lee.ssxy.product.service.SkuPosterService;
import com.lee.ssxy.vo.product.SkuInfoQueryVo;
import com.lee.ssxy.vo.product.SkuInfoVo;
import com.lee.ssxy.vo.product.SkuStockLockVo;
import io.swagger.models.auth.In;
import jodd.bean.BeanUtil;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * sku信息 服务实现类
 * </p>
 *
 * @author phil
 * @since 2023-06-14
 */
@Service
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoMapper, SkuInfo> implements SkuInfoService {
    @Resource
    private SkuImageService skuImageService;
    @Resource
    private SkuAttrValueService skuAttrValueService;
    @Resource
    private SkuPosterService skuPosterService;
    @Resource
    private SkuInfoMapper skuInfoMapper;
    @Resource
    private RabbitService rabbitService;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private RedissonClient redissonClient;
    @Override
    public IPage<SkuInfo> selectSkuPage(Page<SkuInfo> pageParam, SkuInfoQueryVo skuInfoQueryVo) {
        String skuType = skuInfoQueryVo.getSkuType();
        String keyword = skuInfoQueryVo.getKeyword();
        Long categoryId = skuInfoQueryVo.getCategoryId();
        LambdaQueryWrapper<SkuInfo> queryWrapper = new LambdaQueryWrapper<>();
        if(!StringUtils.isEmpty(skuType)){
            queryWrapper.like(SkuInfo::getSkuType,skuType);
        }
        if(!StringUtils.isEmpty(categoryId)){
            queryWrapper.like(SkuInfo::getCategoryId,categoryId);
        }
        if(!StringUtils.isEmpty(keyword)){
            queryWrapper.like(SkuInfo::getSkuName,keyword);
        }
        Page<SkuInfo> skuInfoPage = baseMapper.selectPage(pageParam, queryWrapper);
        return skuInfoPage;
    }

    @Override
    public boolean saveSkuInfo(SkuInfoVo skuInfoVo) {
        SkuInfo skuInfo = new SkuInfo();
        BeanUtils.copyProperties(skuInfoVo,skuInfo);
        SkuPoster skuPoster = new SkuPoster();
        List<SkuPoster> skuPosterList = skuInfoVo.getSkuPosterList();
        for (SkuPoster poster : skuPosterList) {
            skuPoster.setSkuId(skuInfo.getId());
        }
        boolean savePoster = skuPosterService.saveBatch(skuPosterList);
        List<SkuImage> skuImageList = skuInfoVo.getSkuImagesList();
        if(!CollectionUtils.isEmpty(skuImageList)){
            for (SkuImage skuImage : skuImageList) {
                skuImage.setSkuId(skuInfo.getId());
            }
        }
        boolean saveImage = skuImageService.saveBatch(skuImageList);
        List<SkuAttrValue> skuAttrValueList = skuInfoVo.getSkuAttrValueList();
        if(!CollectionUtils.isEmpty(skuAttrValueList)){
            for(SkuAttrValue skuAttrValue:skuAttrValueList){
                skuAttrValue.setSkuId(skuInfo.getId());
            }
        }
        boolean saveAttrValue = skuAttrValueService.saveBatch(skuAttrValueList);
        int insert = baseMapper.insert(skuInfo);
        if(insert > 0){
            return true;
        }else{
            return false;
        }

    }

    /**
     *
     * @param "skuId"
     * @return
     */
    @Override
    public SkuInfo getSkuInfo(Long id) {
        SkuInfoVo skuInfoVo = new SkuInfoVo();

        SkuInfo skuInfo = skuInfoMapper.selectById(id);
        //TODO skuImagesService  skuPosterService  skuAttrValueService分别添加方法
        List<SkuImage> skuImageList = skuImageService.getSkuImageListById(id);
        List<SkuPoster> skuPosterList = skuPosterService.getSkuPosterListById(id);
        List<SkuAttrValue> skuAttrValueList = skuAttrValueService.findBySkuId(id);

        BeanUtils.copyProperties(skuInfo, skuInfoVo);
        skuInfoVo.setSkuImagesList(skuImageList);
        skuInfoVo.setSkuPosterList(skuPosterList);
        skuInfoVo.setSkuAttrValueList(skuAttrValueList);
        return skuInfoVo;
    }

    @Override
    public void updateSkuInfo(SkuInfoVo skuInfoVo) {
        SkuInfo skuInfo = new SkuInfo();
        BeanUtils.copyProperties(skuInfoVo,skuInfo);
        baseMapper.updateById(skuInfoVo);
        Long skuId = skuInfo.getId();
        //删除海报
        List<SkuPoster> skuPosterList = skuInfoVo.getSkuPosterList();
        skuPosterService.remove(new LambdaQueryWrapper<SkuPoster>().eq(SkuPoster::getSkuId,skuId));
        if(!CollectionUtils.isEmpty(skuPosterList)){
            skuPosterService.saveBatch(skuPosterList);
        }
        //删除图片
        List<SkuImage> skuImagesList = skuInfoVo.getSkuImagesList();
        skuImageService.remove(new LambdaQueryWrapper<SkuImage>().eq(SkuImage::getSkuId,skuId));
        if(!CollectionUtils.isEmpty(skuImagesList)){
            skuImageService.saveBatch(skuImagesList);
        }
        List<SkuAttrValue> skuAttrValueList = skuInfoVo.getSkuAttrValueList();
        skuAttrValueService.remove(new LambdaQueryWrapper<SkuAttrValue>().eq(SkuAttrValue::getSkuId,skuId));
        if(!CollectionUtils.isEmpty(skuAttrValueList)){
            skuAttrValueService.saveBatch(skuAttrValueList);
        }
    }

    @Override
    public void check(Long skuId, Integer status) {
        SkuInfo skuInfo  = new SkuInfo();
        skuInfo.setId(skuId);
        skuInfo.setCheckStatus(status);
        baseMapper.updateById(skuInfo);
    }

    @Override
    public void publish(Long skuId, Integer status) {
        if(status == 1) {
            SkuInfo skuInfoUp = new SkuInfo();
            skuInfoUp.setId(skuId);
            skuInfoUp.setPublishStatus(1);
            skuInfoMapper.updateById(skuInfoUp);
            System.out.println("1111111111111111111"+skuId+"111111111111111111111111");
            //商品上架：发送mq消息同步es
            rabbitService.sendMessage(MqConst.EXCHANGE_GOODS_DIRECT, MqConst.ROUTING_GOODS_UPPER, skuId);
        } else {
            SkuInfo skuInfoUp = new SkuInfo();
            skuInfoUp.setId(skuId);
            skuInfoUp.setPublishStatus(0);
            skuInfoMapper.updateById(skuInfoUp);
            System.out.println("0000000000000000000"+skuId+"0000000000000000000000000");

            //商品下架：发送mq消息同步es
            rabbitService.sendMessage(MqConst.EXCHANGE_GOODS_DIRECT, MqConst.ROUTING_GOODS_LOWER, skuId);
        }
    }

    @Override
    public void isNewPerson(Long skuId, Integer status) {
        SkuInfo skuInfo = new SkuInfo();
        skuInfo.setId(skuId);
        skuInfo.setIsNewPerson(status);
    }

    @Override
    public List<SkuInfo> findSkuList(List<Long> skuIdList) {
        List<SkuInfo> list = new ArrayList<>();
        for (Long aLong : skuIdList) {
            list.add(baseMapper.selectOne(new LambdaQueryWrapper<SkuInfo>().eq(SkuInfo::getId,aLong)));
        }
        return list;
    }

    @Override
    public List<SkuInfo>  findSkuInfoByKeyword(String keyword) {
        LambdaQueryWrapper<SkuInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(SkuInfo::getSkuName,keyword);
        List<SkuInfo> skuInfoList = baseMapper.selectList(queryWrapper);
        return skuInfoList;
    }

    @Override
    public List<SkuInfo> findNewPersonSkuInfoList() {
        Page<SkuInfo> pageParam = new Page<>(1,3);
        LambdaQueryWrapper<SkuInfo>  queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SkuInfo::getIsNewPerson,1);
        queryWrapper.eq(SkuInfo::getPublishStatus,1);
        queryWrapper.orderByDesc(SkuInfo::getStock);
        Page<SkuInfo> skuInfoPage = baseMapper.selectPage(pageParam, queryWrapper);
        List<SkuInfo> records = skuInfoPage.getRecords();
        return records;
    }

    @Override
    public SkuInfoVo getSkuInfoVo(Long skuId) {
        SkuInfoVo skuInfoVo = new SkuInfoVo();
        SkuInfo skuInfo = baseMapper.selectById(skuId);
        List<SkuImage> skuImageListById = skuImageService.getSkuImageListById(skuId);
        List<SkuPoster> skuPosterListById = skuPosterService.getSkuPosterListById(skuId);
        List<SkuAttrValue> skuAttrValueList = skuAttrValueService.findBySkuId(skuId);
        BeanUtils.copyProperties(skuInfo,skuInfoVo);
        skuInfoVo.setSkuImagesList(skuImageListById);
        skuInfoVo.setSkuPosterList(skuPosterListById);
        skuInfoVo.setSkuAttrValueList(skuAttrValueList);
        return skuInfoVo;
    }

    @Override
    public Boolean checkAndLock(List<SkuStockLockVo> stockLockVoList, String orderNo) {
        if(!CollectionUtils.isEmpty(stockLockVoList)){
            throw new SsxyException(ResultCodeEnum.DATA_ERROR);
        }
        stockLockVoList.stream().forEach(skuStockLockVo -> this.checkLock(skuStockLockVo));
        boolean flag = stockLockVoList.stream().anyMatch(skuStockLockVo -> !skuStockLockVo.getIsLock());
        if(flag){
            stockLockVoList.stream().filter(SkuStockLockVo::getIsLock)
                    .forEach(skuStockLockVo -> {
                        baseMapper.unlockStock(skuStockLockVo.getSkuId(),skuStockLockVo.getSkuNum()) ;
                    });
            return false;
        }
        redisTemplate.opsForValue().set(RedisConst.SROCK_INFO+orderNo,stockLockVoList);
        return true;
    }

    private void checkLock(SkuStockLockVo skuStockLockVo) {
        RLock rLock = this.redissonClient.getFairLock(RedisConst.SKUKEY_PREFIX + skuStockLockVo.getSkuId());
        rLock.lock();
        try {
            SkuInfo skuInfo = baseMapper.checkStock(skuStockLockVo.getSkuId(),skuStockLockVo.getSkuNum());
            if(skuInfo == null){
                skuStockLockVo.setIsLock(false);
                return;
            }
        Integer rows = baseMapper.lockStock(skuStockLockVo.getSkuId(),skuStockLockVo.getSkuNum());
            if(rows == 1){
                skuStockLockVo.setIsLock(true);
            }
        }finally {
            rLock.unlock();
        }
    }


}
