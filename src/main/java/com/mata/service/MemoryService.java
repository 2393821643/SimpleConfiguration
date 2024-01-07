package com.mata.service;

import com.mata.dto.Result;

public interface MemoryService {
    //获取memory列表 分页
    Result getMemoryList(Integer page);

    //获取单个memory
    Result getMemory(Integer id);

    //获取memory列表总页数
    Result getMemoryPageCount();
}
