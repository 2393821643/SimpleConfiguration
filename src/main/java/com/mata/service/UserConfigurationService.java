package com.mata.service;

import com.mata.dto.Result;
import com.mata.pojo.*;

public interface UserConfigurationService {

    //添加新的配置单
    Result addConfiguration(Integer userId, String configurationName);

    //添加新的cpu到配置单
    Result addCpuToConfiguration(Integer userId, Cpu cpu, String configurationId);

    //根据id找配置
    Result getConfigurationById(String configurationId);

    //获取所有配置单，分页
    Result getAllConfiguration(Integer page);

    //获取当前用户的所有配置单
    Result getAllConfigurationByUser(Integer userId);

    //添加新的gpu到配置单
    Result addGpuToConfiguration(Integer userId, Gpu gpu, String configurationId);

    //添加新的cpufan到配置单
    Result addCpuFanToConfiguration(Integer userId, CpuFan cpuFan, String configurationId);

    //添加新的Mainboard到配置单
    Result addMainboardToConfiguration(Integer userId, Mainboard mainboard, String configurationId);

    //添加新的power到配置单
    Result addPowerToConfiguration(Integer userId, Power power, String configurationId);

    //添加新的chassis到配置单
    Result addChassisToConfiguration(Integer userId, Chassis chassis, String configurationId);

    //添加新的chassis到配置单
    Result addHdToConfiguration(Integer userId, Hd hd, String configurationId);

    //添加新的memory到配置单
    Result addMemoryToConfiguration(Integer userId, Memory memory, String configurationId);

    //删除配置单根据id
    Result removeConfigurationById(Integer userId, String configurationId);


    //获取所有配置单的总页数
    Result getAllConfigurationPageCount();

    //根据索引位置删除配置单的hd
    Result removeHdFromConfiguration(Integer userId, String configurationId, int index);

    //根据索引位置删除配置单的Memory
    Result removeMemoryFromConfiguration(Integer userId, String configurationId, int index);
}
