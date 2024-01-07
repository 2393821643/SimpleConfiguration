package com.mata.controller;

import com.mata.dto.Result;
import com.mata.pojo.Cpu;
import com.mata.service.CpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cpu")
public class CpuController {
    @Autowired
    private CpuService cpuService;

    //获取cpu列表 分页
    @GetMapping("/list/{page}")
    public Result getCpuList(@PathVariable("page") Integer page) {
        return cpuService.getCpuList(page);
    }

    //获取cpu列表总页数
    @GetMapping("/list/page-count")
    public Result getCpuPageCount(){
        return cpuService.getCpuPageCount();
    }

    //获取单个cpu信息
    @GetMapping("/{id}")
    public Result getCpu(@PathVariable("id") Integer id){
        return cpuService.getCpu(id);
    }

}
