package com.lee.ssxy.sys.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.lee.ssxy.model.sys.Region;

import java.util.List;

/**
 * <p>
 * 地区表 服务类
 * </p>
 *
 * @author phil
 * @since 2023-06-13
 */
public interface RegionService extends IService<Region> {

    List<Region> getRegionByKeyword(String keyword);
}
