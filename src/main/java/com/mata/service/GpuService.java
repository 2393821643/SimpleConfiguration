package com.mata.service;

import com.mata.dto.Result;

public interface GpuService {
    //获取gpu列表 分页
    public Result getGpuList(Integer page);

    //获取单个gpu
    public Result getGpu(Integer id);

    //获取gpu列表总页数
    Result getGpuPageCount();
}
