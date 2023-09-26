package com.lee.ssxy.acl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lee.ssxy.acl.mapper.PermissionMapper;
import com.lee.ssxy.acl.service.PermissionService;
import com.lee.ssxy.acl.service.RolePermissionService;
import com.lee.ssxy.acl.util.PermissionHelper;
import com.lee.ssxy.model.acl.Permission;
import com.lee.ssxy.model.acl.Role;
import com.lee.ssxy.model.acl.RolePermission;
import jdk.nashorn.internal.ir.LiteralNode;
import org.springframework.stereotype.Service;
import sun.net.ftp.FtpDirEntry;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {
    @Resource
    private RolePermissionService rolePermissionService;
    @Override
    public void removeChildById(Long id) {
        List<Long> idList = new ArrayList<>();
        findAllPermissionId(id,idList);
        idList.add(id);
        baseMapper.deleteBatchIds(idList);

    }

    @Override
    public List<Permission> queryAllPermission() {
        List<Permission> list = baseMapper.selectList(null);
        List<Permission> result = PermissionHelper.buildPermission(list);
        return result;
    }

    @Override
    public Map<String,Object> getRolePermissions(Long id) {
        List<Permission> allPermissions = queryAllPermission();
        LambdaQueryWrapper<RolePermission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RolePermission::getRoleId,id);
        List<RolePermission> list = rolePermissionService.list(queryWrapper);
        List<Permission> allRolesPermission = new ArrayList<>();
        List<Long> idList = list.stream().map(item -> item.getPermissionId()).collect(Collectors.toList());
        for (Permission p : allPermissions) {
            if(idList.contains(p.getId())){
                allRolesPermission.add(p);
            }
        }
        Map<String,Object> map = new HashMap<>();
        map.put("allPermissions",allPermissions);
        map.put("assignPermissions",allRolesPermission);
        return map;

    }


    private void findAllPermissionId(Long id, List<Long> idList) {
        LambdaQueryWrapper<Permission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Permission::getPid,id);
        List<Permission> permissions = baseMapper.selectList(queryWrapper);
        permissions.stream().forEach(item->{
            idList.add(item.getId());
            this.findAllPermissionId(id,idList);
        });
    }
}
