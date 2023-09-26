package com.lee.ssxy.acl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lee.ssxy.model.acl.AdminRole;

public interface AdminRoleService extends IService<AdminRole> {
    boolean saveRoleAdmin(Long roleId, Long[] permissionId);
}
