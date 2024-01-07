package com.mata.controller;

import com.mata.dto.Result;
import com.mata.service.MemoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/memory")
public class MemoryController {
    @Autowired
    private MemoryService memoryService;

    //获取memory列表 分页
    @GetMapping("/list/{page}")
    public Result getMemoryList(@PathVariable("page") Integer page) {
        return memoryService.getMemoryList(page);
    }

    //获取memory列表总页数
    @GetMapping("/list/page-count")
    public Result getMemoryPageCount(){
        return memoryService.getMemoryPageCount();
    }

    //获取单个memory信息
    @GetMapping("/{id}")
    public Result getMemory(@PathVariable("id") Integer id){
        return memoryService.getMemory(id);
    }
}