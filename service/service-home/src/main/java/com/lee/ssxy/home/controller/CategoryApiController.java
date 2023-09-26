package com.lee.ssxy.home.controller;

import com.lee.ssxy.client.product.ProductFeignClient;
import com.lee.ssxy.common.result.Result;
import com.lee.ssxy.model.product.Category;
import com.lee.ssxy.model.search.SkuEs;
import com.lee.ssxy.vo.search.SkuEsQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("api/home")
public class CategoryApiController {

    @Resource
    private ProductFeignClient productFeignClient;

    @GetMapping("category")
    public Result categoryList(){
        List<Category> allCategoryList = productFeignClient.findAllCategoryList();
        return Result.ok(allCategoryList);
    }

}
