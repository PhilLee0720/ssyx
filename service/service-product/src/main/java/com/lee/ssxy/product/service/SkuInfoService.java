package com.lee.ssxy.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lee.ssxy.model.product.SkuInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lee.ssxy.vo.product.SkuInfoQueryVo;
import com.lee.ssxy.vo.product.SkuInfoVo;
import com.lee.ssxy.vo.product.SkuStockLockVo;
import io.swagger.models.auth.In;

import java.util.List;

/**
 * <p>
 * sku信息 服务类
 * </p>
 *
 * @author phil
 * @since 2023-06-14
 */
public interface SkuInfoService extends IService<SkuInfo> {

    IPage<SkuInfo> selectSkuPage(Page<SkuInfo> pageParam, SkuInfoQueryVo skuInfoQueryVo);

    boolean saveSkuInfo(SkuInfoVo skuInfoVo);

    SkuInfo getSkuInfo(Long id);

    void updateSkuInfo(SkuInfoVo skuInfoVo);

    void check(Long skuId, Integer status);

    void publish(Long skuId, Integer status);

    void isNewPerson(Long skuId, Integer status);

    List<SkuInfo> findSkuList(List<Long> skuIdList);

    List<SkuInfo> findSkuInfoByKeyword(String keyword);

    List<SkuInfo> findNewPersonSkuInfoList();

    SkuInfoVo getSkuInfoVo(Long skuId);

    Boolean checkAndLock(List<SkuStockLockVo> stockLockVoList, String orderNo);
}
