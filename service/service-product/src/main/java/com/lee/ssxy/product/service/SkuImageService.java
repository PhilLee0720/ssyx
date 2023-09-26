package com.lee.ssxy.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lee.ssxy.model.product.SkuImage;
import com.lee.ssxy.model.product.SkuInfo;

import java.util.List;

/**
 * <p>
 * 商品图片 服务类
 * </p>
 *
 * @author phil
 * @since 2023-06-14
 */
public interface SkuImageService extends IService<SkuImage> {

    List<SkuImage> getSkuImageListById(Long id);
}
