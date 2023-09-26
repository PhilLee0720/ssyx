package com.lee.ssxy.acl.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lee.ssxy.model.acl.Admin;
import com.lee.ssxy.common.result.Result;
import com.lee.ssxy.vo.acl.AdminQueryVo;

public interface AdminService extends IService<Admin> {
    IPage<Admin> selectUserPage(Page<Admin> page, AdminQueryVo adminQueryVo);

    Result saveUser(Admin admin);

}
