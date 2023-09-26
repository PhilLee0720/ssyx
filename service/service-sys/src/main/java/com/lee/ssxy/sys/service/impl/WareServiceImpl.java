package com.lee.ssxy.sys.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lee.ssxy.model.sys.Ware;
import com.lee.ssxy.sys.mapper.WareMapper;
import com.lee.ssxy.sys.service.WareService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lee.ssxy.vo.product.WareQueryVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 仓库表 服务实现类
 * </p>
 *
 * @author phil
 * @since 2023-06-13
 */
@Service
public class WareServiceImpl extends ServiceImpl<WareMapper, Ware> implements WareService {

    @Override
    public IPage<Ware> queryWare(Page<Ware> pageParam, WareQueryVo wareQueryVo) {
        String name = wareQueryVo.getName();
        LambdaQueryWrapper<Ware> queryWrapper = new LambdaQueryWrapper<>();
        if(!StringUtils.isEmpty(name)){
            queryWrapper.like(Ware::getName,name);
        }
        IPage<Ware> pageResult = baseMapper.selectPage(pageParam, queryWrapper);
        return pageResult;
    }
}
