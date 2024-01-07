package com.mata.service;

import com.mata.dto.Result;

public interface PowerService {
    //获取Power列表 分页
    Result getPowerList(Integer page);

    //获取单个Power
    Result getPower(Integer id);

    //获取power列表总页数
    Result getPowerPageCount();
}
