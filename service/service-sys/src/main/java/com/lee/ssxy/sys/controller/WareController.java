package com.lee.ssxy.sys.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lee.ssxy.model.sys.Ware;
import com.lee.ssxy.common.result.Result;
import com.lee.ssxy.sys.service.WareService;
import com.lee.ssxy.vo.product.WareQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 仓库表 前端控制器
 * </p>
 *
 * @author phil
 * @since 2023-06-13
 */
@Api(tags = "仓库接口")
@RestController
@RequestMapping("/admin/sys/ware")
public class WareController {
    @Resource
    private WareService wareService;

    @GetMapping("/{page}/{limit}")
    @ApiOperation("查询仓库列表")
    public Result pageList(@PathVariable("page")Long page, @PathVariable("limit")Long limit,
                           WareQueryVo wareQueryVo){
        Page<Ware> pageParam = new Page<>(page,limit);
        IPage<Ware> pageModel = wareService.queryWare(pageParam,wareQueryVo);
        return Result.ok(pageModel);
    }

    @GetMapping("/findAllList")
    @ApiOperation("查询所有仓库列表")
    public Result findAllList(){
        List<Ware> list = wareService.list();
        return Result.ok(list);
    }
}

