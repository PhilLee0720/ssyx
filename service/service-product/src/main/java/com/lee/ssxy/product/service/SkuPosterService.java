package com.lee.ssxy.product.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.lee.ssxy.model.product.SkuPoster;

import java.util.List;

/**
 * <p>
 * 商品海报表 服务类
 * </p>
 *
 * @author phil
 * @since 2023-06-14
 */
public interface SkuPosterService extends IService<SkuPoster> {

    List<SkuPoster> getSkuPosterListById(Long id);
}
