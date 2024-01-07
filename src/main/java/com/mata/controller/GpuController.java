package com.mata.controller;

import com.mata.dto.Result;
import com.mata.service.GpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gpu")
public class GpuController {
    @Autowired
    private GpuService gpuService;

    //获取gpu列表 分页
    @GetMapping("/list/{page}")
    public Result getGpuList(@PathVariable("page") Integer page) {
        return gpuService.getGpuList(page);
    }

    //获取gpu列表总页数
    @GetMapping("/list/page-count")
    public Result getGpuPageCount(){
        return gpuService.getGpuPageCount();
    }

    //获取单个gpu信息
    @GetMapping("/{id}")
    public Result getGpu(@PathVariable("id") Integer id){
        return gpuService.getGpu(id);
    }

}
