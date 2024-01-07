package com.mata.service;

import com.mata.dto.Result;

public interface MainboardService {
    //获取Mainboard列表 分页
    Result getMainboardList(Integer page);

    //获取单个Mainboard
    Result getMainboard(Integer id);

    //获取mainboard列表总页数
    Result getMainboardPageCount();
}
