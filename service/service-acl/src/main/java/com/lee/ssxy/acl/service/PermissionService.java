package com.lee.ssxy.acl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lee.ssxy.model.acl.Permission;

import java.util.List;
import java.util.Map;

public interface PermissionService extends IService<Permission> {
    void removeChildById(Long id);

    List<Permission> queryAllPermission();

     Map<String,Object> getRolePermissions(Long id);
}
