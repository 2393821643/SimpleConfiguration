package com.mata.controller;

import com.mata.dto.Result;
import com.mata.service.ChassisService;
import com.mata.service.GpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chassis")
public class ChassisController {
    @Autowired
    private ChassisService chassisService;

    //获取chassis列表 分页
    @GetMapping("/list/{page}")
    public Result getChassisList(@PathVariable("page") Integer page) {
        return chassisService.getChassisList(page);
    }

    //获取chassis列表总页数
    @GetMapping("/list/page-count")
    public Result getChassisPageCount(){
        return chassisService.getChassisPageCount();
    }

    //获取单个chassis信息
    @GetMapping("/{id}")
    public Result getChassis(@PathVariable("id") Integer id){
        return chassisService.getChassis(id);
    }
}
