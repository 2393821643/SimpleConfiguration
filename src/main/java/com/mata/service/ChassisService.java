package com.mata.service;

import com.mata.dto.Result;

public interface ChassisService {
    //获取Chassis列表 分页
    Result getChassisList(Integer page);

    //获取单个Chassis
    Result getChassis(Integer id);

    //获取chassis列表总页数
    Result getChassisPageCount();
}
