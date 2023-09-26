package com.lee.ssxy.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lee.ssxy.model.sys.Region;
import com.lee.ssxy.sys.mapper.RegionMapper;
import com.lee.ssxy.sys.service.RegionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 地区表 服务实现类
 * </p>
 *
 * @author phil
 * @since 2023-06-13
 */
@Service
public class RegionServiceImpl extends ServiceImpl<RegionMapper,Region> implements RegionService {

    @Override
    public List<Region> getRegionByKeyword(String keyword) {
        LambdaQueryWrapper<Region> queryWrapper = new LambdaQueryWrapper<>();
        if(!StringUtils.isEmpty(keyword)){
            queryWrapper.like(Region::getName,keyword);
        }
        List<Region> regions = baseMapper.selectList(queryWrapper);
        return regions;
    }
}
