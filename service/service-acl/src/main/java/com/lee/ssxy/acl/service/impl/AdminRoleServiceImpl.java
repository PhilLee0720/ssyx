package com.lee.ssxy.acl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lee.ssxy.acl.mapper.AdminRoleMapper;
import com.lee.ssxy.acl.service.AdminRoleService;
import com.lee.ssxy.model.acl.Admin;
import com.lee.ssxy.model.acl.AdminRole;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminRoleServiceImpl extends ServiceImpl<AdminRoleMapper, AdminRole> implements AdminRoleService {
    @Override
    public boolean saveRoleAdmin(Long roleId, Long[] permissionId) {
         LambdaQueryWrapper<AdminRole> queryWrapper = new LambdaQueryWrapper<>();
         queryWrapper.eq(AdminRole::getRoleId,roleId);
        List<AdminRole> adminRoles = baseMapper.selectList(queryWrapper);
        List<Long> idList = adminRoles.stream().map(item -> {
            return item.getId();
        }).collect(Collectors.toList());
        baseMapper.deleteBatchIds(idList);
        List<AdminRole> ARList = new ArrayList<>();
        for (Long id : permissionId) {
            AdminRole adminRole = new AdminRole();
            adminRole.setRoleId(roleId);
            adminRole.setAdminId(id);
            ARList.add(adminRole);
        }
        boolean saveBatch = this.saveBatch(ARList);
        return saveBatch;
    }
}
