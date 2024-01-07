package com.mata.service;

import com.mata.dto.Result;

public interface CpuFanService {
    //获取cpufan列表 分页
    Result getCpuFanList(Integer page);

    //获取单个gpu
    Result getCpuFan(Integer id);

    //获取cpufan列表总页数
    Result getCpuFanPageCount();

}
