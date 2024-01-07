package com.mata.service.serviceImpl;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mata.dao.GpuDao;
import com.mata.dao.PowerDao;
import com.mata.dto.Result;
import com.mata.pojo.Gpu;
import com.mata.pojo.Power;
import com.mata.service.GpuService;
import com.mata.service.PowerService;
import com.mata.util.RedisConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class PowerServiceImpl implements PowerService {
    @Autowired
    private PowerDao powerDao;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 获取power列表
     * @param page 当前页
     * @return powerList/message
     */
    @Override
    public Result getPowerList(Integer page) {
        //查Redis缓存
        String powerListJson = stringRedisTemplate.opsForValue().get(RedisConstants.POWER_CACHE_PAGE_KEY + page);
        //非空（null / ""）存在返回
        if (StrUtil.isNotBlank(powerListJson)) {
            List<Power> powerList = JSONUtil.toList(powerListJson, Power.class);
            return new Result(powerList,null, null);
        }
        //如果是“”
        if(powerListJson !=null){
            return new Result(null,null, null);
        }
        //尝试获取锁
        String lockKey = RedisConstants.POWER_CACHE_PAGE_LOCK+page;
        List powerList=null;
        try {
            //获取失败，休眠
            if(!tryLock(lockKey)){
                Thread.sleep(50);
                getPowerList(page);
            }
            //获取成功
            //查数据库，页面条件 每次返回20个结构
            IPage resultPage = new Page(page, 20);
            LambdaQueryWrapper<Power> wrapper=new LambdaQueryWrapper<>();
            wrapper.select(Power::getPowerId,Power::getPowerName,Power::getPowerPrice,Power::getPowerImg);
            powerDao.selectPage(resultPage, wrapper);
            //获取power列表
            powerList = resultPage.getRecords();
            //如果返回的power列表是空值，将建立空白缓存(缓存穿透)
            if(powerList.size()==0){
                stringRedisTemplate.opsForValue().set(RedisConstants.POWER_CACHE_PAGE_KEY + page, "",
                        RedisConstants.CACHE_NULL_TTL, TimeUnit.MINUTES);
                return null;
            }
            //获取总页数
            long powerPageCount=resultPage.getPages();
            //转换json 存入redis 存活时间10min
            powerListJson = JSONUtil.toJsonStr(powerList);
            stringRedisTemplate.opsForValue().set(RedisConstants.POWER_CACHE_PAGE_KEY + page, powerListJson,
                    RedisConstants.CACHE_PAGE_TTL, TimeUnit.MINUTES);
            //存页数
            stringRedisTemplate.opsForValue().set(RedisConstants.POWER_CACHE_PAGE_COUNT_KEY, Long.toString(powerPageCount),
                    RedisConstants.CACHE_PAGE_TTL,TimeUnit.MINUTES);

        }catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            //释放互斥锁
            unlock(lockKey);
        }
        //返回数据
        return new Result(powerList, null, null);

    }

    /**
     * 获取单个power
     * @param id power的id
     * @return power的信息
     */
    @Override
    public Result getPower(Integer id) {
        //查Redis缓存
        String powerJson = stringRedisTemplate.opsForValue().get(RedisConstants.POWER_CACHE_KEY + id);
        //非空（null / ""）存在返回
        if (StrUtil.isNotBlank(powerJson)) {
            Power resultPower = JSONUtil.toBean(powerJson,Power.class);
            return new Result(resultPower,null, null);
        }
        //如果是“”
        if(powerJson !=null){
            return new Result(null,null, null);
        }
        //尝试获取锁
        String lockKey = RedisConstants.POWER_CACHE_LOCK+id;
        Power power=null;
        try {
            //获取失败，休眠
            if(!tryLock(lockKey)){
                Thread.sleep(50);
                getPower(id);
            }
            //获取成功
            //查数据库 获取power
            power = powerDao.selectById(id);
            //如果返回的power列表是空值，将建立空白缓存(缓存穿透)
            if(power==null){
                stringRedisTemplate.opsForValue().set(RedisConstants.POWER_CACHE_KEY + id, "",
                        RedisConstants.CACHE_NULL_TTL, TimeUnit.MINUTES);
                return null;
            }
            //转换json 存入redis 存活时间10min
            powerJson = JSONUtil.toJsonStr(power);
            stringRedisTemplate.opsForValue().set(RedisConstants.POWER_CACHE_KEY + id, powerJson,
                    RedisConstants.CACHE_TTL, TimeUnit.MINUTES);

        }catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            //释放互斥锁
            unlock(lockKey);
        }
        //返回数据
        return new Result(power, null, null);
    }

    /**
     * 返回页数
     *
     * @return 返回页数
     */
    @Override
    public Result getPowerPageCount() {
        //获取页数
        String pageCount = stringRedisTemplate.opsForValue().get(RedisConstants.POWER_CACHE_PAGE_COUNT_KEY);
        //重置时间
        stringRedisTemplate.expire(RedisConstants.POWER_CACHE_PAGE_COUNT_KEY,RedisConstants.CACHE_TTL, TimeUnit.MINUTES);
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
