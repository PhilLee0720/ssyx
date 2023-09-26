package com.lee.ssxy.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lee.ssxy.model.product.Attr;

import java.util.List;

/**
 * <p>
 * 商品属性 服务类
 * </p>
 *
 * @author phil
 * @since 2023-06-14
 */
public interface AttrService extends IService<Attr> {

    List<Attr> findByAttrGroupId(Long attrGroupId);
}
