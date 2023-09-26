package com.lee.ssxy.acl.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lee.ssxy.acl.service.AdminService;
import com.lee.ssxy.acl.service.RoleService;
import com.lee.ssxy.common.auth.AuthContextHolder;
import com.lee.ssxy.model.acl.Admin;
import com.lee.ssxy.common.result.Result;
import com.lee.ssxy.vo.acl.AdminQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Api(tags = "管理员用户接口")
@RestController
@RequestMapping("/admin/acl/user")
public class AdminController {
    @Resource
    private AdminService adminService;
    @Resource
    private RoleService roleService;
    @ApiOperation("用户列表")
    @GetMapping("/{page}/{limit}")
    public Result pageList(@PathVariable("page")Long pageNo,
                           @PathVariable("limit") Long limit,
                           AdminQueryVo adminQueryVo){
        Page<Admin> page = new Page<>(pageNo,limit);
        IPage<Admin> pageModel = adminService.selectUserPage(page, adminQueryVo);
        return Result.ok(pageModel);
    }

    @ApiOperation("根据ID查询用户")
    @GetMapping("/{id}")
    public Result getById(@PathVariable("id") Long id){
        Admin admin = adminService.getById(id);
        System.out.println(AuthContextHolder.getWareId());
        return Result.ok(admin);
    }

    @ApiOperation("获取用户角色")
    @GetMapping("/toAssign/{adminId}")
    public Result getRoles(@PathVariable("adminId") Long adminId){
        Map<String,Object> result = roleService.getRolesByAdminId(adminId);
        return Result.ok(result);
    }

    @ApiOperation("为用户分配角色")
    @GetMapping("/doAssign")
    public Result doAssign(@RequestParam Long adminId,
                           @RequestParam Long[] roleIDs) {
        roleService.saveAdminRole(adminId,roleIDs);
        return Result.ok(null);
    }
    @ApiOperation("修改用户")
    @PutMapping("/update")
    public Result update(@RequestBody Admin admin){
        boolean update = adminService.updateById(admin );
        if(!update){
            return Result.fail(null);
        }
        return  Result.ok(null);
    }

    @ApiOperation("保存新用户")
    @PostMapping("/save")
    public Result add(@RequestBody Admin admin){
        return adminService.saveUser(admin);
    }

    @ApiOperation("根据ID删除一个用户")
    @DeleteMapping("/remove/{id}")
    public Result removeByID(@PathVariable("id") Long id){
        boolean remove = adminService.removeById(id);
        if(!remove){
            return Result.fail(null);
        }
        return Result.ok(null);
    }
    @ApiOperation("删除多个用户")
    @DeleteMapping("/batchRemove")
    public Result removeUsers(@RequestBody List<Long> list){
        boolean remove = adminService.removeByIds(list);
        if(!remove){
            return Result.fail(null);
        }
        return Result.ok(null);
    }
}
