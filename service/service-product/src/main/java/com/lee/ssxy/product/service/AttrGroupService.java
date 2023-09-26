package com.lee.ssxy.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lee.ssxy.model.product.AttrGroup;
import com.lee.ssxy.vo.product.AttrGroupQueryVo;

;import java.util.List;

/**
 * <p>
 * 属性分组 服务类
 * </p>
 *
 * @author phil
 * @since 2023-06-14
 */
public interface AttrGroupService extends IService<AttrGroup> {

    IPage<AttrGroup> selectPage(Page<AttrGroup> pageParam, AttrGroupQueryVo attrGroupQueryVo);

    List<AttrGroup> findAllList();
}
