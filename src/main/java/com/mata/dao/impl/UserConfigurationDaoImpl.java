package com.mata.dao.impl;

import cn.hutool.core.util.IdUtil;
import com.mata.dao.UserConfigurationDao;
import com.mata.pojo.*;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class UserConfigurationDaoImpl implements UserConfigurationDao {
    @Autowired
    private MongoTemplate mongoTemplate;

    //添加新的配置单
    @Override
    public void addConfiguration(Integer userId, String configurationName) {
        UserConfiguration userConfiguration = new UserConfiguration();
        //生成一个uuid
        userConfiguration.setId(IdUtil.simpleUUID());
        userConfiguration.setUserId(userId);
        userConfiguration.setConfigurationName(configurationName);
        mongoTemplate.insert(userConfiguration);

    }

    //获取配置单的用户id
    @Override
    public Integer getConfigurationUserId(String configurationId) {
        UserConfiguration userConfiguration = mongoTemplate.findById(configurationId, UserConfiguration.class);
        return userConfiguration.getUserId();
    }

    //根据id获取配置单
    @Override
    public UserConfiguration getConfigurationById(String configurationId) {
        return mongoTemplate.findById(configurationId, UserConfiguration.class);
    }


    //获取所有配置单
    @Override
    public List<UserConfiguration> getAllConfiguration(Integer page) {
        Query query = new Query();
        query.skip((page - 1) * 20L).limit(20);
        query.fields().include("_id", "userId", "configurationName");
        return mongoTemplate.find(query, UserConfiguration.class);
    }

    //获取所有配置单的总页数
    @Override
    public long getAllConfigurationPageCount() {
        return mongoTemplate.count(new Query(), UserConfiguration.class);
    }



    //获取当前用户的所有配置单
    @Override
    public List<UserConfiguration> getAllConfigurationByUser(Integer userId) {
        Query query = new Query(Criteria.where("userId").is(userId));
        query.fields().include("_id", "userId", "configurationName");
        return mongoTemplate.find(query, UserConfiguration.class);
    }


    //添加新cpu
    @Override
    public void addCpuToConfiguration(Cpu cpu, String configurationId) {
        Cpu cpuDto = new Cpu();
        cpuDto.setCpuId(cpu.getCpuId());
        cpuDto.setCpuName(cpu.getCpuName());
        cpuDto.setCpuPrice(cpu.getCpuPrice());
        Query query = new Query(Criteria.where("_id").is(configurationId));
        Update update = new Update();
        update.set("cpu", cpuDto);
        mongoTemplate.upsert(query, update, UserConfiguration.class);
    }

    //添加新gpu
    @Override
    public void addGpuToConfiguration(Gpu gpu, String configurationId) {
        Gpu gpuDto = new Gpu();
        gpuDto.setGpuId(gpu.getGpuId());
        gpuDto.setGpuName(gpu.getGpuName());
        gpuDto.setGpuPrice(gpu.getGpuPrice());
        Query query = new Query(Criteria.where("_id").is(configurationId));
        Update update = new Update();
        update.set("gpu", gpuDto);
        mongoTemplate.upsert(query, update, UserConfiguration.class);
    }

    //添加新cpuFan
    @Override
    public void addCpuFanToConfiguration(CpuFan cpuFan, String configurationId) {
        CpuFan cpuFanDto = new CpuFan();
        cpuFanDto.setCpuFanId(cpuFan.getCpuFanId());
        cpuFanDto.setCpuFanName(cpuFan.getCpuFanName());
        cpuFanDto.setCpuFanPrice(cpuFan.getCpuFanPrice());
        Query query = new Query(Criteria.where("_id").is(configurationId));
        Update update = new Update();
        update.set("cpuFan", cpuFanDto);
        mongoTemplate.upsert(query, update, UserConfiguration.class);
    }

    //添加新的Mainboard到配置单
    @Override
    public void addMainboardToConfiguration(Mainboard mainboard, String configurationId) {
        Mainboard mainboardDto = new Mainboard();
        mainboardDto.setMainboardId(mainboard.getMainboardId());
        mainboardDto.setMainboardName(mainboard.getMainboardName());
        mainboardDto.setMainboardPrice(mainboard.getMainboardPrice());
        Query query = new Query(Criteria.where("_id").is(configurationId));
        Update update = new Update();
        update.set("mainboard", mainboardDto);
        mongoTemplate.upsert(query, update, UserConfiguration.class);
    }

    //添加新的power到配置单
    @Override
    public void addPowerToConfiguration(Power power, String configurationId) {
        Power powerDto = new Power();
        powerDto.setPowerId(power.getPowerId());
        powerDto.setPowerName(power.getPowerName());
        powerDto.setPowerPrice(power.getPowerPrice());
        Query query = new Query(Criteria.where("_id").is(configurationId));
        Update update = new Update();
        update.set("power", powerDto);
        mongoTemplate.upsert(query, update, UserConfiguration.class);
    }

    //添加新的Chassis到配置单
    @Override
    public void addChassisToConfiguration(Chassis chassis, String configurationId) {
        Chassis chassisDto = new Chassis();
        chassisDto.setChassisId(chassis.getChassisId());
        chassisDto.setChassisName(chassis.getChassisName());
        chassisDto.setChassisPrice(chassis.getChassisPrice());
        Query query = new Query(Criteria.where("_id").is(configurationId));
        Update update = new Update();
        update.set("chassis", chassisDto);
        mongoTemplate.upsert(query, update, UserConfiguration.class);
    }


    //添加新的hd到配置单
    @Override
    public void addHdToConfiguration(Hd hd, String configurationId) {
        Hd hdDto = new Hd();
        hdDto.setHdId(hd.getHdId());
        hdDto.setHdName(hd.getHdName());
        hdDto.setHdPrice(hd.getHdPrice());
        Query query = new Query(Criteria.where("_id").is(configurationId));
        Update update = new Update();
        update.push("hdList",hdDto);
        mongoTemplate.upsert(query, update, UserConfiguration.class);
    }


    //添加新的memory到配置单
    @Override
    public void addMemoryToConfiguration(Memory memory, String configurationId) {
        Memory memoryDto = new Memory();
        memoryDto.setMemoryId(memory.getMemoryId());
        memoryDto.setMemoryName(memory.getMemoryName());
        memoryDto.setMemoryPrice(memory.getMemoryPrice());
        Query query = new Query(Criteria.where("_id").is(configurationId));
        Update update = new Update();
        update.push("memoryList",memoryDto);
        UpdateResult updateResult = mongoTemplate.upsert(query, update, UserConfiguration.class);
    }

    //删除配置单
    @Override
    public void removeConfigurationById(String configurationId) {
        Query query=new Query(Criteria.where("_id").is(configurationId));
        mongoTemplate.remove(query, UserConfiguration.class);
    }


    //删除hd从配置单
    @Override
    public void removeHdFromConfiguration(String configurationId, int index) {
        Query query=new Query(Criteria.where("_id").is(configurationId));
        UserConfiguration configuration = mongoTemplate.findById(configurationId, UserConfiguration.class);
        List<Hd> hdList = configuration.getHdList();
        hdList.remove(index);
        Update update=new Update();
        update.set("hdList",hdList);
        mongoTemplate.updateFirst(query,update,UserConfiguration.class);
    }


    //删除memory从配置单
    @Override
    public void removeMemoryFromConfiguration(String configurationId, int index) {
        Query query=new Query(Criteria.where("_id").is(configurationId));
        UserConfiguration configuration = mongoTemplate.findById(configurationId, UserConfiguration.class);
        List<Memory> memoryList = configuration.getMemoryList();
        memoryList.remove(index);
        Update update=new Update();
        update.set("memoryList",memoryList);
        mongoTemplate.updateFirst(query,update,UserConfiguration.class);
    }

}
