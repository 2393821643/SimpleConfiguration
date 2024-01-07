package com.mata.controller;

import com.mata.dto.Result;
import com.mata.service.CpuFanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cpu-fan")
public class CpuFanController {
    @Autowired
    private CpuFanService cpuFanService;

    //获取cpufan列表 分页
    @GetMapping("/list/{page}")
    public Result getCpuFanList(@PathVariable("page") Integer page) {
        return cpuFanService.getCpuFanList(page);
    }

    //获取cpufan列表总页数
    @GetMapping("/list/page-count")
    public Result getCpuFanPageCount(){
        return cpuFanService.getCpuFanPageCount();
    }

    //获取单个cpufan信息
    @GetMapping("/{id}")
    public Result getCpuFan(@PathVariable("id") Integer id) {
        return cpuFanService.getCpuFan(id);
    }

}
