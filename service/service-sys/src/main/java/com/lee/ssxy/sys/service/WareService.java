package com.lee.ssxy.sys.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lee.ssxy.model.sys.Ware;
import com.lee.ssxy.vo.product.WareQueryVo;

/**
 * <p>
 * 仓库表 服务类
 * </p>
 *
 * @author phil
 * @since 2023-06-13
 */
public interface WareService extends IService<Ware> {

    IPage<Ware> queryWare(Page<Ware> pageParam, WareQueryVo wareQueryVo);
}
