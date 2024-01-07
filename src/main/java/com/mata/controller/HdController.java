package com.mata.controller;

import com.mata.dto.Result;
import com.mata.service.GpuService;
import com.mata.service.HdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hd")
public class HdController {
    @Autowired
    private HdService hdService;

    //获取hd列表 分页
    @GetMapping("/list/{page}")
    public Result getHdList(@PathVariable("page") Integer page) {
        return hdService.getHdList(page);
    }

    //获取单个hd信息
    @GetMapping("/{id}")
    public Result getHd(@PathVariable("id") Integer id){
        return hdService.getHd(id);
    }

    //获取hd列表总页数
    @GetMapping("/list/page-count")
    public Result getHdPageCount(){
        return hdService.getHdPageCount();
    }
}