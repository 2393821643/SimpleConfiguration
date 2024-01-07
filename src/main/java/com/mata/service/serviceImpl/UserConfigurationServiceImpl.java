package com.mata.service.serviceImpl;

import com.mata.dao.UserConfigurationDao;
import com.mata.dto.Code;
import com.mata.dto.Result;
import com.mata.pojo.*;
import com.mata.service.UserConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserConfigurationServiceImpl implements UserConfigurationService {
    @Autowired
    UserConfigurationDao userConfigurationDao;

    /**
     * 添加新的配置单
     *
     * @param userId            用户的id
     * @param configurationName 配置单的名称
     */
    @Override
    public Result addConfiguration(Integer userId, String configurationName) {
        //判断格式
        if (configurationName.length() > 20 || configurationName.length() == 0) {
            return new Result(null, Code.CREATE_CONFIGURATION_ERR, "配置单名称格式错误");
        }
        //添加
        userConfigurationDao.addConfiguration(userId, configurationName);
        return new Result(null, Code.CREATE_CONFIGURATION_OK, "添加新配置单成功");
    }


    /**
     * 根据id获取配置单
     *
     * @param configurationId 配置单id
     */
    @Override
    public Result getConfigurationById(String configurationId) {
        UserConfiguration userConfiguration = userConfigurationDao.getConfigurationById(configurationId);
        return new Result(userConfiguration, null, null);
    }

    /**
     * 获取所有配置单
     *
     * @param page 当前页
     */
    @Override
    public Result getAllConfiguration(Integer page) {
        List<UserConfiguration> userConfigurationList = userConfigurationDao.getAllConfiguration(page);
        return new Result(userConfigurationList, null, null);
    }

    /**
     * 获取所有配置单
     *
     * @return  总页页
     */
    @Override
    public Result getAllConfigurationPageCount() {
        long allCount=userConfigurationDao.getAllConfigurationPageCount();
        if(allCount%20==0){
            Integer pageCount= Math.toIntExact(allCount / 20);
            return new Result(pageCount, null, null);
        }
        Integer pageCount= Math.toIntExact((allCount / 20) + 1);
        return new Result(pageCount, null, null);
    }


    /**
     * 获取当前用户的所有配置单
     *
     * @param userId 用户的id
     */
    @Override
    public Result getAllConfigurationByUser(Integer userId) {
        List<UserConfiguration> userConfigurationList = userConfigurationDao.getAllConfigurationByUser(userId);
        return new Result(userConfigurationList, null, null);
    }

    /**
     * 添加cpu到配置单
     *
     * @param userId          用户的id
     * @param cpu             cpu信息
     * @param configurationId 配置单id
     */
    @Override
    public Result addCpuToConfiguration(Integer userId, Cpu cpu, String configurationId) {
        try {
            //获取配置单的userId
            Integer resultUserId = userConfigurationDao.getConfigurationUserId(configurationId);
            if (!resultUserId.equals(userId)) {
                return new Result(null, Code.ADD_CONFIGURATIONS_ERR, "这不是你的配置单");
            }
            //添加（更新）配置
            userConfigurationDao.addCpuToConfiguration(cpu, configurationId);
            return new Result(null, Code.ADD_CONFIGURATIONS_OK, "添加成功");
        } catch (NullPointerException e) {
            return new Result(null, Code.ADD_CONFIGURATIONS_ERR, "不存在此配置单");
        }

    }

    /**
     * 添加gpu到配置单
     *
     * @param userId          用户的id
     * @param gpu             gpu信息
     * @param configurationId 配置单id
     */
    @Override
    public Result addGpuToConfiguration(Integer userId, Gpu gpu, String configurationId) {
        try {
            //获取配置单的userId
            Integer resultUserId = userConfigurationDao.getConfigurationUserId(configurationId);
            if (!resultUserId.equals(userId)) {
                return new Result(null, Code.ADD_CONFIGURATIONS_ERR, "这不是你的配置单");
            }
            //添加（更新）配置
            userConfigurationDao.addGpuToConfiguration(gpu, configurationId);
            return new Result(null, Code.ADD_CONFIGURATIONS_OK, "添加成功");
        } catch (NullPointerException e) {
            return new Result(null, Code.ADD_CONFIGURATIONS_ERR, "不存在此配置单");
        }
    }

    /**
     * 添加cpufan到配置单
     *
     * @param userId          用户的id
     * @param cpuFan          cpuFan信息
     * @param configurationId 配置单id
     */
    @Override
    public Result addCpuFanToConfiguration(Integer userId, CpuFan cpuFan, String configurationId) {
        try {
            //获取配置单的userId
            Integer resultUserId = userConfigurationDao.getConfigurationUserId(configurationId);
            if (!resultUserId.equals(userId)) {
                return new Result(null, Code.ADD_CONFIGURATIONS_ERR, "这不是你的配置单");
            }
            //添加（更新）配置
            userConfigurationDao.addCpuFanToConfiguration(cpuFan, configurationId);
            return new Result(null, Code.ADD_CONFIGURATIONS_OK, "添加成功");
        } catch (NullPointerException e) {
            return new Result(null, Code.ADD_CONFIGURATIONS_ERR, "不存在此配置单");
        }
    }

    /**
     * 添加mainboard到配置单
     *
     * @param userId          用户的id
     * @param mainboard       mainboard信息
     * @param configurationId 配置单id
     */
    @Override
    public Result addMainboardToConfiguration(Integer userId, Mainboard mainboard, String configurationId) {
        try {
            //获取配置单的userId
            Integer resultUserId = userConfigurationDao.getConfigurationUserId(configurationId);
            if (!resultUserId.equals(userId)) {
                return new Result(null, Code.ADD_CONFIGURATIONS_ERR, "这不是你的配置单");
            }
            //添加（更新）配置
            userConfigurationDao.addMainboardToConfiguration(mainboard, configurationId);
            return new Result(null, Code.ADD_CONFIGURATIONS_OK, "添加成功");
        } catch (NullPointerException e) {
            return new Result(null, Code.ADD_CONFIGURATIONS_ERR, "不存在此配置单");
        }
    }

    /**
     * 添加power到配置单
     *
     * @param userId          用户的id
     * @param power           power信息
     * @param configurationId 配置单id
     */
    @Override
    public Result addPowerToConfiguration(Integer userId, Power power, String configurationId) {
        try {
            //获取配置单的userId
            Integer resultUserId = userConfigurationDao.getConfigurationUserId(configurationId);
            if (!resultUserId.equals(userId)) {
                return new Result(null, Code.ADD_CONFIGURATIONS_ERR, "这不是你的配置单");
            }
            //添加（更新）配置
            userConfigurationDao.addPowerToConfiguration(power, configurationId);
            return new Result(null, Code.ADD_CONFIGURATIONS_OK, "添加成功");
        } catch (NullPointerException e) {
            return new Result(null, Code.ADD_CONFIGURATIONS_ERR, "不存在此配置单");
        }
    }

    /**
     * 添加Chassis到配置单
     *
     * @param userId          用户的id
     * @param chassis          chassis信息
     * @param configurationId 配置单id
     */
    @Override
    public Result addChassisToConfiguration(Integer userId, Chassis chassis, String configurationId) {
        try {
            //获取配置单的userId
            Integer resultUserId = userConfigurationDao.getConfigurationUserId(configurationId);
            if (!resultUserId.equals(userId)) {
                return new Result(null, Code.ADD_CONFIGURATIONS_ERR, "这不是你的配置单");
            }
            //添加（更新）配置
            userConfigurationDao.addChassisToConfiguration(chassis, configurationId);
            return new Result(null, Code.ADD_CONFIGURATIONS_OK, "添加成功");
        } catch (NullPointerException e) {
            return new Result(null, Code.ADD_CONFIGURATIONS_ERR, "不存在此配置单");
        }
    }

    /**
     * 添加hd到配置单
     *
     * @param userId          用户的id
     * @param hd       hd信息
     * @param configurationId 配置单id
     */
    @Override
    public Result addHdToConfiguration(Integer userId, Hd hd, String configurationId) {
        try {
            //获取配置单的userId
            Integer resultUserId = userConfigurationDao.getConfigurationUserId(configurationId);
            if (!resultUserId.equals(userId)) {
                return new Result(null, Code.ADD_CONFIGURATIONS_ERR, "这不是你的配置单");
            }
            //添加（更新）配置
            userConfigurationDao.addHdToConfiguration(hd, configurationId);
            return new Result(null, Code.ADD_CONFIGURATIONS_OK, "添加成功");
        } catch (NullPointerException e) {
            return new Result(null, Code.ADD_CONFIGURATIONS_ERR, "不存在此配置单");
        }
    }


    /**
     * 添加memory到配置单
     *
     * @param userId          用户的id
     * @param memory       memory信息
     * @param configurationId 配置单id
     */
    @Override
    public Result addMemoryToConfiguration(Integer userId, Memory memory, String configurationId) {
        try {
            //获取配置单的userId
            Integer resultUserId = userConfigurationDao.getConfigurationUserId(configurationId);
            if (!resultUserId.equals(userId)) {
                return new Result(null, Code.ADD_CONFIGURATIONS_ERR, "这不是你的配置单");
            }
           //添加（更新）配置
            userConfigurationDao.addMemoryToConfiguration(memory, configurationId);
            return new Result(null, Code.ADD_CONFIGURATIONS_OK, "添加成功");
        } catch (NullPointerException e) {
            return new Result(null, Code.ADD_CONFIGURATIONS_ERR, "不存在此配置单");
        }
    }

    /**
     * 删除配置单根据配置单id
     *
     * @param userId          用户的id
     * @param configurationId 配置单id
     */
    @Override
    public Result removeConfigurationById(Integer userId, String configurationId) {
        try {
            //获取配置单的userId
            Integer resultUserId = userConfigurationDao.getConfigurationUserId(configurationId);
            if (!resultUserId.equals(userId)) {
                return new Result(null, Code.ADD_CONFIGURATIONS_ERR, "这不是你的配置单");
            }
            //删除
            userConfigurationDao.removeConfigurationById(configurationId);
            return new Result(null, Code.ADD_CONFIGURATIONS_OK, "删除成功");
        } catch (NullPointerException e) {
            return new Result(null, Code.ADD_CONFIGURATIONS_ERR, "不存在此配置单");
        }
    }


    /**
     * 删除hd从配置单
     *
     * @param userId          用户的id
     * @param index      hd索引
     * @param configurationId 配置单id
     */
    @Override
    public Result removeHdFromConfiguration(Integer userId, String configurationId, int index) {
        try {
            //获取配置单的userId
            Integer resultUserId = userConfigurationDao.getConfigurationUserId(configurationId);
            if (!resultUserId.equals(userId)) {
                return new Result(null, Code.ADD_CONFIGURATIONS_ERR, "这不是你的配置单");
            }
            //删除
            userConfigurationDao.removeHdFromConfiguration(configurationId,index);
            return new Result(null, Code.ADD_CONFIGURATIONS_OK, "删除成功");
        } catch (NullPointerException e) {
            return new Result(null, Code.ADD_CONFIGURATIONS_ERR, "不存在此配置单");
        }
    }

    /**
     * 删除memory从配置单
     *
     * @param userId          用户的id
     * @param index      hd索引
     * @param configurationId 配置单id
     */
    @Override
    public Result removeMemoryFromConfiguration(Integer userId, String configurationId, int index) {
        try {
            //获取配置单的userId
            Integer resultUserId = userConfigurationDao.getConfigurationUserId(configurationId);
            if (!resultUserId.equals(userId)) {
                return new Result(null, Code.ADD_CONFIGURATIONS_ERR, "这不是你的配置单");
            }
            //删除
            userConfigurationDao.removeMemoryFromConfiguration(configurationId,index);
            return new Result(null, Code.ADD_CONFIGURATIONS_OK, "删除成功");
        } catch (NullPointerException e) {
            return new Result(null, Code.ADD_CONFIGURATIONS_ERR, "不存在此配置单");
        }
    }


}
