package com.mata.service.serviceImpl;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mata.dao.ChassisDao;
import com.mata.dto.Result;
import com.mata.pojo.Chassis;
import com.mata.service.ChassisService;
import com.mata.util.RedisConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ChassisServiceImpl implements ChassisService {
    @Autowired
    private ChassisDao chassisDao;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 获取chassis列表
     * @param page 当前页
     * @return chassisList/message
     */
    @Override
    public Result getChassisList(Integer page) {
        //查Redis缓存
        String chassisListJson = stringRedisTemplate.opsForValue().get(RedisConstants.CHASSIS_CACHE_PAGE_KEY + page);
        //非空（null / ""）存在返回
        if (StrUtil.isNotBlank(chassisListJson)) {
            List<Chassis> chassisList = JSONUtil.toList(chassisListJson, Chassis.class);
            return new Result(chassisList,null, null);
        }
        //如果是“”
        if(chassisListJson !=null){
            return new Result(null,null, null);
        }
        //尝试获取锁
        String lockKey = RedisConstants.CHASSIS_CACHE_PAGE_LOCK+page;
        List chassisList=null;
        try {
            //获取失败，休眠
            if(!tryLock(lockKey)){
                Thread.sleep(50);
                getChassisList(page);
            }
            //获取成功
            //查数据库，页面条件 每次返回20个结构
            IPage resultPage = new Page(page, 20);
            LambdaQueryWrapper<Chassis> wrapper=new LambdaQueryWrapper<>();
            wrapper.select(Chassis::getChassisId,Chassis::getChassisName,Chassis::getChassisPrice,Chassis::getChassisImg);
            chassisDao.selectPage(resultPage, wrapper);
            //获取chassis列表
            chassisList = resultPage.getRecords();
            //如果返回的chassis列表是空值，将建立空白缓存(缓存穿透)
            if(chassisList.size()==0){
                stringRedisTemplate.opsForValue().set(RedisConstants.CHASSIS_CACHE_PAGE_KEY + page, "",
                        RedisConstants.CACHE_NULL_TTL, TimeUnit.MINUTES);
                return null;
            }
            //获取总页数
            long chassisPageCount=resultPage.getPages();
            //转换json 存入redis 存活时间10min
            chassisListJson = JSONUtil.toJsonStr(chassisList);
            stringRedisTemplate.opsForValue().set(RedisConstants.CHASSIS_CACHE_PAGE_KEY + page, chassisListJson,
                    RedisConstants.CACHE_PAGE_TTL, TimeUnit.MINUTES);
            //存页数
            stringRedisTemplate.opsForValue().set(RedisConstants.CHASSIS_CACHE_PAGE_COUNT_KEY, Long.toString(chassisPageCount),
                    RedisConstants.CACHE_PAGE_TTL,TimeUnit.MINUTES);

        }catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            //释放互斥锁
            unlock(lockKey);
        }
        //返回数据
        return new Result(chassisList, null, null);

    }

    /**
     * 获取单个chassis
     * @param id chassis的id
     * @return chassis的信息
     */
    @Override
    public Result getChassis(Integer id) {
        //查Redis缓存
        String chassisJson = stringRedisTemplate.opsForValue().get(RedisConstants.CHASSIS_CACHE_KEY + id);
        //非空（null / ""）存在返回
        if (StrUtil.isNotBlank(chassisJson)) {
            Chassis resultChassis = JSONUtil.toBean(chassisJson,Chassis.class);
            return new Result(resultChassis,null, null);
        }
        //如果是“”
        if(chassisJson !=null){
            return new Result(null,null, null);
        }
        //尝试获取锁
        String lockKey = RedisConstants.CHASSIS_CACHE_LOCK+id;
        Chassis chassis=null;
        try {
            //获取失败，休眠
            if(!tryLock(lockKey)){
                Thread.sleep(50);
                getChassis(id);
            }
            //获取成功
            //查数据库 获取chassis
            chassis = chassisDao.selectById(id);
            //如果返回的chassis列表是空值，将建立空白缓存(缓存穿透)
            if(chassis==null){
                stringRedisTemplate.opsForValue().set(RedisConstants.CHASSIS_CACHE_KEY + id, "",
                        RedisConstants.CACHE_NULL_TTL, TimeUnit.MINUTES);
                return null;
            }
            //转换json 存入redis 存活时间10min
            chassisJson = JSONUtil.toJsonStr(chassis);
            stringRedisTemplate.opsForValue().set(RedisConstants.CHASSIS_CACHE_KEY + id, chassisJson,
                    RedisConstants.CACHE_TTL, TimeUnit.MINUTES);

        }catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            //释放互斥锁
            unlock(lockKey);
        }
        //返回数据
        return new Result(chassis, null, null);
    }

    /**
     * 返回页数
     *
     * @return 返回页数
     */
    @Override
    public Result getChassisPageCount() {
        //获取页数
        String pageCount = stringRedisTemplate.opsForValue().get(RedisConstants.CHASSIS_CACHE_PAGE_COUNT_KEY);
        //重置时间
        stringRedisTemplate.expire(RedisConstants.CHASSIS_CACHE_PAGE_COUNT_KEY,RedisConstants.CACHE_TTL, TimeUnit.MINUTES);
        return new Result(Integer.parseInt(pageCount),null,null);
    }

    /**
     * 尝试获取锁
     *
     * @param key 代入的锁的key
     */
    private boolean tryLock(String key) {
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(key, "1",
                RedisConstants.CACHE_LOCK_TTL, TimeUnit.SECONDS);
        return BooleanUtil.isTrue(flag);
    }


    /**
     * 释放锁
     *
     * @param key 要释放的锁
     */
    private void unlock(String key) {
        stringRedisTemplate.delete(key);
    }
}
