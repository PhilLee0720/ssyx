package com.lee.ssxy.acl.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lee.ssxy.model.acl.Role;
import com.lee.ssxy.vo.acl.RoleQueryVo;
import org.springframework.stereotype.Service;

import java.util.Map;


public interface RoleService extends IService<Role> {
    IPage<Role> selectRolePage(Page<Role> page, RoleQueryVo roleQueryVo);

    Map<String, Object> getRolesByAdminId(Long adminId);

    void saveAdminRole(Long adminId, Long[] roleIDs);
}
