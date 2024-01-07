package com.mata.service;

import com.mata.dto.Result;

public interface CpuService {
    //获取cpu列表 分页
    Result getCpuList(Integer page);

    //获取单个cpu
    Result getCpu(Integer id);

    //获取cpu列表总页数
    Result getCpuPageCount();
}
