package com.lee.ssxy.product.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lee.ssxy.model.product.SkuInfo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * sku信息 Mapper 接口
 * </p>
 *
 * @author phil
 * @since 2023-06-14
 */
public interface SkuInfoMapper extends BaseMapper<SkuInfo> {

    void unlockStock(@Param("skuId") Long skuId,@Param("skuNum") Integer skuNum);

    SkuInfo checkStock(@Param("skuId") Long skuId,@Param("skuNum") Integer skuNum);

    Integer lockStock(@Param("skuId") Long skuId,@Param("skuNum") Integer skuNum);
}
