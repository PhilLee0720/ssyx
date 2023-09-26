package com.lee.ssxy.product.controller;

import com.lee.ssxy.product.service.FileUploadService;
import com.lee.ssxy.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@Api(tags = "文件上传")
@RestController
public class FileUploadController {
    @Resource
    private FileUploadService fileUploadService;
    @ApiOperation("图片上传")
    @PostMapping("/fileUpload")
    public Result fileUpload(MultipartFile file){
        String url = fileUploadService.uploadFile(file);
        return Result.ok(url);
    }
}
