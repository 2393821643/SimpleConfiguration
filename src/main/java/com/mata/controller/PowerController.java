package com.mata.controller;

import com.mata.dto.Result;
import com.mata.service.PowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/power")
public class PowerController {
    @Autowired
    private PowerService powerService;

    //获取power列表 分页
    @GetMapping("/list/{page}")
    public Result getPowerList(@PathVariable("page") Integer page) {
        return powerService.getPowerList(page);
    }

    //获取power列表总页数
    @GetMapping("/list/page-count")
    public Result getPowerPageCount(){
        return powerService.getPowerPageCount();
    }

    //获取单个power信息
    @GetMapping("/{id}")
    public Result getPower(@PathVariable("id") Integer id){
        return powerService.getPower(id);
    }
}
