package com.mata.service;

import com.mata.dto.Result;

public interface HdService {
    //获取hd列表 分页
    Result getHdList(Integer page);

    //获取单个hd
    Result getHd(Integer id);

    //获取hd列表总页数
    Result getHdPageCount();
}
