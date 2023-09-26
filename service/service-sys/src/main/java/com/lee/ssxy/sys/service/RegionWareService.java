package com.lee.ssxy.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lee.ssxy.common.exception.SsxyException;
import com.lee.ssxy.model.sys.RegionWare;
import com.lee.ssxy.vo.sys.RegionWareQueryVo;

/**
 * <p>
 * 城市仓库关联表 服务类
 * </p>
 *
 * @author phil
 * @since 2023-06-13
 */
public interface RegionWareService extends IService<RegionWare> {

    IPage<RegionWare> queryRegionWarePage(Page<RegionWare> pageParam, RegionWareQueryVo regionWareQueryVo);

    boolean saveRegionWare(RegionWare regionWare) throws SsxyException;

    boolean updateStatus(Long id, Integer status);
}
