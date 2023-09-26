package com.lee.ssxy.acl.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lee.ssxy.acl.service.RoleService;
import com.lee.ssxy.model.acl.Role;
import com.lee.ssxy.common.result.Result;
import com.lee.ssxy.vo.acl.RoleQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "角色接口")
@RestController
@RequestMapping("/admin/acl/role")
public class RoleController {

    @Resource
    private RoleService roleService;
    @ApiOperation("角色分页查询")
    @GetMapping("{current}/{limit}")
    public Result pageList(@PathVariable("current") Long current,
                           @PathVariable("limit")Long limit,
                           RoleQueryVo roleQueryVo ){

        Page<Role> pageParam= new Page<>(current,limit);
        IPage<Role> pageModel = roleService.selectRolePage(pageParam,roleQueryVo);

        return Result.ok(pageModel);
    }

    @ApiOperation("角色添加")
    @PostMapping("/save")
    public Result save(@RequestBody Role role){
        System.out.println(role.getRoleName());
        boolean save = roleService.save(role);
        if(!save){
            Result.fail(null);
        }
        return Result.ok(null);
    }
    @ApiOperation("根据id查询角色")
    @GetMapping("/get/{id}")
    public Result get(@PathVariable("id") Long id){
        Role role = roleService.getById(id);
        return Result.ok(role);
    }
    @ApiOperation("根据id修改角色")
    @PutMapping("/update")
    public Result update(@RequestBody Role role){
        boolean update = roleService.updateById(role);
        if(!update){
            return Result.fail(null);
        }
        return Result.ok(null);
    }
//    @ApiOperation("获取一个角色的所有权限列表")
//    @GetMapping("/toAssign/{id}")
//    public Result getAssign(@PathVariable("id") Long id){
//
//    }


    @ApiOperation("根据ID删除角色")
    @DeleteMapping("/remove/{id}")
    public Result removeById(@PathVariable("id") Long id){
        boolean remove = roleService.removeById(id);
        if(!remove){
            return Result.fail(null);
        }
        return  Result.ok(null);
    }

    @ApiOperation("批量删除多个角色")
    @DeleteMapping("/batchRemove")
    public Result removeRoles(@RequestBody List<Long> ids){
        boolean remove = roleService.removeByIds(ids);
        if(!remove){
            return Result.fail(null);
        }
        return Result.ok(null);
    }

}
