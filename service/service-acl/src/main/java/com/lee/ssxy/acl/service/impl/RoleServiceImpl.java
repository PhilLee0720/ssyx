package com.lee.ssxy.acl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lee.ssxy.acl.mapper.AdminRoleMapper;
import com.lee.ssxy.acl.mapper.RoleMapper;
import com.lee.ssxy.acl.service.AdminRoleService;
import com.lee.ssxy.acl.service.RoleService;
import com.lee.ssxy.model.acl.Admin;
import com.lee.ssxy.model.acl.AdminRole;
import com.lee.ssxy.model.acl.Role;
import com.lee.ssxy.vo.acl.RoleQueryVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService   {
    @Resource
    private AdminRoleService adminRoleService;
    @Override
    public IPage<Role> selectRolePage(Page<Role> pageParam, RoleQueryVo roleQueryVo) {
        String roleName = roleQueryVo.getRoleName();
        LambdaQueryWrapper<Role> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(!StringUtils.isEmpty(roleName)){
            lambdaQueryWrapper.like(Role::getRoleName,roleName);
        }
        Page<Role> page = baseMapper.selectPage(pageParam, lambdaQueryWrapper);
       return page;

    }

    @Override
    public Map<String, Object> getRolesByAdminId(Long adminId) {
        List<Role> allRoleList = baseMapper.selectList(null);
        LambdaQueryWrapper<AdminRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AdminRole::getRoleId,adminId);
        List<AdminRole>  adminRoleList = adminRoleService.list(queryWrapper);
        List<Long> roleIdList = adminRoleList
                .stream()
                .map(item -> item.getRoleId())
                .collect(Collectors.toList());
        List<Role> assginRoleList = new ArrayList<>();
        for(Role role : allRoleList){
            if(roleIdList.contains(role.getId())){
                assginRoleList.add(role);
            }
        }
        Map<String,Object> map = new HashMap<>();
        map.put("allRolesList",allRoleList);
        map.put("assignRoles",assginRoleList);
        return map;
    }

    @Override
    public void saveAdminRole(Long adminId, Long[] roleIDs) {
        LambdaQueryWrapper<AdminRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AdminRole::getAdminId,adminId);
        adminRoleService.remove(queryWrapper);
        for (Long roleID : roleIDs) {
            AdminRole adminRole = new AdminRole();
            adminRole.setAdminId(adminId);
            adminRole.setRoleId(roleID);
            adminRoleService.save(adminRole);
        }
    }
}
