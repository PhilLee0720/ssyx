package com.lee.ssxy.product.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.lee.ssxy.model.product.SkuAttrValue;

import java.util.List;

/**
 * <p>
 * spu属性值 服务类
 * </p>
 *
 * @author phil
 * @since 2023-06-14
 */
public interface SkuAttrValueService extends IService<SkuAttrValue> {

    List<SkuAttrValue> findBySkuId(Long id);
}
