package com.mata.dao;

import com.mata.pojo.*;

import java.util.List;

public interface UserConfigurationDao {
    //添加新的配置单
    public void addConfiguration(Integer userId, String configurationName);

    //获取配置单的用户id;
    public Integer getConfigurationUserId(String configurationId);

    //添加新cpu
    public void addCpuToConfiguration(Cpu cpu, String configurationId);

    //根据id获取当前配置单
    public UserConfiguration getConfigurationById(String configurationId);

    //获取所有配置单
    public List<UserConfiguration> getAllConfiguration(Integer page);

    //获取当前用户的所有配置单
    public List<UserConfiguration> getAllConfigurationByUser(Integer userId);

    //添加新gpu
    public void addGpuToConfiguration(Gpu gpu, String configurationId);

    //添加新CpuFan
    public void addCpuFanToConfiguration(CpuFan cpuFan, String configurationId);

    //添加新mainboard
    void addMainboardToConfiguration(Mainboard mainboard, String configurationId);

    //添加新power
    void addPowerToConfiguration(Power power, String configurationId);

    //添加新chassis
    void addChassisToConfiguration(Chassis chassis, String configurationId);


    //添加新hd
    void addHdToConfiguration(Hd hd, String configurationId);

    //添加新memory
    void addMemoryToConfiguration(Memory memory, String configurationId);

    //删除配置单
    void removeConfigurationById(String configurationId);


    //获取所有配置单的总页数
    long getAllConfigurationPageCount();

    //删除hd从配置单
    void removeHdFromConfiguration(String configurationId, int index);

    //删除Memory从配置单
    void removeMemoryFromConfiguration(String configurationId, int index);
}
