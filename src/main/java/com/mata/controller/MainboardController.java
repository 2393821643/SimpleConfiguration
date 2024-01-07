package com.mata.controller;

import com.mata.dto.Result;
import com.mata.service.MainboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mainboard")
public class MainboardController {
    @Autowired
    private MainboardService mainboardService;

    //获取mainboard列表 分页
    @GetMapping("/list/{page}")
    public Result getMainboardList(@PathVariable("page") Integer page) {
        return mainboardService.getMainboardList(page);
    }

    //获取mainboard列表总页数
    @GetMapping("/list/page-count")
    public Result getMainboardPageCount(){
        return mainboardService.getMainboardPageCount();
    }

    //获取单个mainboard信息
    @GetMapping("/{id}")
    public Result getMainboard(@PathVariable("id") Integer id){
        return mainboardService.getMainboard(id);
    }
}