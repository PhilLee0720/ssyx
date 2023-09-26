package com.lee.ssxy.acl.controller;

import com.lee.ssxy.acl.service.AdminRoleService;
import com.lee.ssxy.acl.service.PermissionService;
import com.lee.ssxy.model.acl.Permission;
import com.lee.ssxy.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Api(tags = "菜单管理接口")
@RestController
@RequestMapping("/admin/acl/permission")
public class PermissionController {
    @Resource
    private PermissionService permissionService;
    @Resource
    private AdminRoleService adminRoleService;

    @ApiOperation("查询所有菜单")
    @GetMapping
    public Result list(){
        List<Permission> list = permissionService.queryAllPermission();
        return Result.ok(list);
    }

    @ApiOperation("删除一个权限项")
    @DeleteMapping("/remove/{id}")
    public Result removeById(@PathVariable("id")Long id){
           permissionService.removeChildById(id);
           return Result.ok(null);
    }

    @ApiOperation("保存一个权限项")
    @PostMapping("/save")
    public Result save(@RequestBody Permission permission){
        boolean save = permissionService.save(permission);
        if(!save){
            return Result.fail(null);
        }else{
            return  Result.ok(null);
        }
    }

    @ApiOperation("查看某个角色的权限列表")
    @GetMapping("/toAssign/{roleId}")
    public Result toAssign(@PathVariable("roleId")Long id){
        Map<String,Object> list = permissionService.getRolePermissions(id);
        return Result.ok(list);
    }

    @ApiOperation("为角色分配权限")
    @PostMapping("/doAssign")
    public Result doAssign(@RequestParam Long roleId,@RequestParam Long[] permissionId){
        boolean save = adminRoleService.saveRoleAdmin(roleId,permissionId);
        if(!save){
            return Result.fail(null);
        }else{
            return Result.ok(null);
        }
    }

    @ApiOperation("更新一个权限项目")
    @PutMapping("/update")
    public Result update(@RequestBody Permission permission){
        boolean update = permissionService.updateById(permission);
        if(!update){
            return  Result.fail(null);
        }else{
            return Result.ok(null);
        }
    }



}
