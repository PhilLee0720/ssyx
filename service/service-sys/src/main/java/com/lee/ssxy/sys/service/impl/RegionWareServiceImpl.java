package com.lee.ssxy.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lee.ssxy.common.exception.SsxyException;
import com.lee.ssxy.model.sys.RegionWare;
import com.lee.ssxy.common.result.ResultCodeEnum;
import com.lee.ssxy.sys.mapper.RegionWareMapper;
import com.lee.ssxy.sys.service.RegionWareService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lee.ssxy.vo.sys.RegionWareQueryVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 城市仓库关联表 服务实现类
 * </p>
 *
 * @author phil
 * @since 2023-06-13
 */
@Service
public class RegionWareServiceImpl extends ServiceImpl<RegionWareMapper, RegionWare> implements RegionWareService {

    @Override
    public IPage<RegionWare> queryRegionWarePage(Page<RegionWare> pageParam,RegionWareQueryVo regionWareQueryVo) {
        String keyword = regionWareQueryVo.getKeyword();
        LambdaQueryWrapper<RegionWare> queryWrapper = new LambdaQueryWrapper<>();
        if(!StringUtils.isEmpty(keyword)){
            queryWrapper.like(RegionWare::getWareName,keyword).or().like(RegionWare::getRegionName,keyword);
        }
        IPage<RegionWare> pageResult = baseMapper.selectPage(pageParam, queryWrapper);
        return pageResult;

    }

    @Override
    public boolean saveRegionWare(RegionWare regionWare) throws SsxyException {
        LambdaQueryWrapper<RegionWare> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RegionWare::getId,regionWare.getId());
        Integer count = baseMapper.selectCount(queryWrapper);
        if(count  > 0){
            throw new SsxyException(ResultCodeEnum.REGION_OPEN);
        }
        int insert = baseMapper.insert(regionWare);
        if(insert > 0){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        RegionWare regionWare = baseMapper.selectById(id);
        regionWare.setStatus(status);
        int i = baseMapper.updateById(regionWare);
        if(i > 0){
            return true;
        }else{
            return false;
        }
    }
}
