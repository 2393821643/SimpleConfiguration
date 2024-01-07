package com.mata.controller;

import com.mata.dto.Result;
import com.mata.pojo.*;
import com.mata.service.UserConfigurationService;
import com.mata.util.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user-configuration")
public class UserConfigurationController {
    @Autowired
    private UserConfigurationService userConfigurationService;

    //添加新的配置单
    @PostMapping("/{configurationName}")
    public Result addConfiguration(@PathVariable("configurationName") String configurationName) {
        Integer userId = UserHolder.getUser().getUserId();
        return userConfigurationService.addConfiguration(userId, configurationName);
    }

    //根据id获取配置单
    @GetMapping("/{configurationId}")
    public Result getConfigurationById(@PathVariable("configurationId") String configurationId) {
        return userConfigurationService.getConfigurationById(configurationId);
    }

    //获取所有配置单，分页
    @GetMapping("/all-list/{page}")
    public Result getAllConfiguration(@PathVariable("page") Integer page) {
        return userConfigurationService.getAllConfiguration(page);
    }


    //获取所有配置单的总页数
    @GetMapping("/all-list/page-count")
    public Result getAllConfigurationPageCount() {
        return userConfigurationService.getAllConfigurationPageCount();
    }

    //获取当前用户所有配置单
    @GetMapping("/user/all-list")
    public Result getAllConfigurationByUser() {
        Integer userId = UserHolder.getUser().getUserId();
        return userConfigurationService.getAllConfigurationByUser(userId);
    }


    //根据id删除当前用户的配置单
    @DeleteMapping("/user/{configurationId}")
    public Result removeConfigurationById(@PathVariable("configurationId") String configurationId) {
        Integer userId = UserHolder.getUser().getUserId();
        return userConfigurationService.removeConfigurationById(userId, configurationId);
    }

    //将Cpu添加到配置单中
    @PostMapping("/cpu/{configurationId}")
    public Result addCpuToConfiguration(@RequestBody Cpu cpu,
                                        @PathVariable("configurationId") String configurationId) {
        Integer userId = UserHolder.getUser().getUserId();
        return userConfigurationService.addCpuToConfiguration(userId, cpu, configurationId);
    }

    //将Gpu添加到配置单中
    @PostMapping("/gpu/{configurationId}")
    public Result addGpuToConfiguration(@RequestBody Gpu gpu,
                                        @PathVariable("configurationId") String configurationId) {
        Integer userId = UserHolder.getUser().getUserId();
        return userConfigurationService.addGpuToConfiguration(userId, gpu, configurationId);
    }

    //将cpufan添加到配置单中
    @PostMapping("/cpu-fan/{configurationId}")
    public Result addCpuFanToConfiguration(@RequestBody CpuFan cpuFan,
                                           @PathVariable("configurationId") String configurationId) {
        Integer userId = UserHolder.getUser().getUserId();
        return userConfigurationService.addCpuFanToConfiguration(userId, cpuFan, configurationId);
    }

    //将mainboard添加到配置单中
    @PostMapping("/mainboard/{configurationId}")
    public Result addMainboardToConfiguration(@RequestBody Mainboard mainboard,
                                              @PathVariable("configurationId") String configurationId) {
        Integer userId = UserHolder.getUser().getUserId();
        return userConfigurationService.addMainboardToConfiguration(userId, mainboard, configurationId);
    }


    //将power添加到配置单中
    @PostMapping("/power/{configurationId}")
    public Result addPowerToConfiguration(@RequestBody Power power,
                                          @PathVariable("configurationId") String configurationId) {
        Integer userId = UserHolder.getUser().getUserId();
        return userConfigurationService.addPowerToConfiguration(userId, power, configurationId);
    }

    //将chassis添加到配置单中
    @PostMapping("/chassis/{configurationId}")
    public Result addChassisToConfiguration(@RequestBody Chassis chassis,
                                            @PathVariable("configurationId") String configurationId) {
        Integer userId = UserHolder.getUser().getUserId();
        return userConfigurationService.addChassisToConfiguration(userId, chassis, configurationId);
    }


    //将hd添加到配置单中
    @PostMapping("/hd/{configurationId}")
    public Result addHdToConfiguration(@RequestBody Hd hd,
                                       @PathVariable("configurationId") String configurationId) {
        Integer userId = UserHolder.getUser().getUserId();
        return userConfigurationService.addHdToConfiguration(userId, hd, configurationId);
    }

    //将memory添加到配置单中
    @PostMapping("/memory/{configurationId}")
    public Result addMemoryToConfiguration(@RequestBody Memory memory,
                                           @PathVariable("configurationId") String configurationId) {
        Integer userId = UserHolder.getUser().getUserId();
        return userConfigurationService.addMemoryToConfiguration(userId, memory, configurationId);
    }

    //根据索引位置删除配置单的hd
    @DeleteMapping("/hd/{configurationId}/{index}")
    public Result removeHdFromConfiguration(@PathVariable("configurationId") String configurationId,
                                            @PathVariable("index") int index) {
        Integer userId = UserHolder.getUser().getUserId();
        return userConfigurationService.removeHdFromConfiguration(userId, configurationId, index);
    }

    //根据索引位置删除配置单的memory
    @DeleteMapping("/memory/{configurationId}/{index}")
    public Result removeMemoryFromConfiguration(@PathVariable("configurationId") String configurationId,
                                            @PathVariable("index") int index) {
        Integer userId = UserHolder.getUser().getUserId();
        return userConfigurationService.removeMemoryFromConfiguration(userId, configurationId, index);
    }


}
