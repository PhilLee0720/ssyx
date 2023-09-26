package com.lee.ssxy.product.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lee.ssxy.model.product.AttrGroup;
import com.lee.ssxy.product.mapper.AttrGroupMapper;
import com.lee.ssxy.product.service.AttrGroupService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lee.ssxy.vo.product.AttrGroupQueryVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.smartcardio.ATR;
import java.util.List;

/**
 * <p>
 * 属性分组 服务实现类
 * </p>
 *
 * @author phil
 * @since 2023-06-14
 */
@Service
 public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupMapper, AttrGroup> implements AttrGroupService {

 @Override
 public IPage<AttrGroup> selectPage(Page<AttrGroup> pageParam, AttrGroupQueryVo attrGroupQueryVo) {
  String name = attrGroupQueryVo.getName();
  LambdaQueryWrapper<AttrGroup> queryWrapper  = new LambdaQueryWrapper<>();
  if(!StringUtils.isEmpty(name)){
   queryWrapper.like(AttrGroup::getName,name);
  }
  Page<AttrGroup> attrGroupPage = baseMapper.selectPage(pageParam, queryWrapper);
  return attrGroupPage;
 }

 @Override
 public List<AttrGroup> findAllList() {
  QueryWrapper<AttrGroup> queryWrapper = new QueryWrapper<>();
  queryWrapper.orderByDesc("id");
  List<AttrGroup> attrGroups = baseMapper.selectList(queryWrapper);
  return attrGroups;

 }
}
