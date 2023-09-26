package com.lee.ssxy.acl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lee.ssxy.acl.mapper.AdminMapper;
import com.lee.ssxy.acl.service.AdminService;
import com.lee.ssxy.common.utils.MD5;
import com.lee.ssxy.model.acl.Admin;
import com.lee.ssxy.common.result.Result;
import com.lee.ssxy.vo.acl.AdminQueryVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {


    @Override
    public IPage<Admin> selectUserPage(Page<Admin> pageParam, AdminQueryVo adminQueryVo) {
        String username = adminQueryVo.getUsername();
        LambdaQueryWrapper<Admin> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(!StringUtils.isEmpty(username)){
            lambdaQueryWrapper.eq(Admin::getUsername,username);
        }
        Page<Admin> adminPage = baseMapper.selectPage(pageParam, lambdaQueryWrapper);
        return adminPage;
    }

    @Override
    public Result saveUser(Admin admin) {
        String password = admin.getPassword();
        if(!StringUtils.isEmpty(password)){
            MD5.encrypt(password);
        }
        admin.setPassword(password);
        boolean save = this.save(admin);
        if(!save){
            return Result.fail(null );
        }else{
            return Result.ok(null);
        }
    }


}
