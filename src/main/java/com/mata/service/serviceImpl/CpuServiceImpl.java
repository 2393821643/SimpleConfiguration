package com.mata.service.serviceImpl;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mata.dao.CpuDao;
import com.mata.dto.Result;
import com.mata.pojo.Cpu;
import com.mata.service.CpuService;
import com.mata.util.RedisConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class CpuServiceImpl implements CpuService {
    @Autowired
    private CpuDao cpuDao;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    /**
     * 获取cpu列表
     * @param page 当前页
     * @return cpuList/message
    */
    @Override
    public Result getCpuList(Integer page) {
        //查Redis缓存
        String cpuListJson = stringRedisTemplate.opsForValue().get(RedisConstants.CPU_CACHE_PAGE_KEY + page);
        //非空（null / ""）存在返回
        if (StrUtil.isNotBlank(cpuListJson)) {
            List<Cpu> cpuList = JSONUtil.toList(cpuListJson, Cpu.class);
            return new Result(cpuList,null, null);
        }
        //如果是“”
        if(cpuListJson !=null){
            return new Result(null,null, null);
        }
        //尝试获取锁
        String lockKey = RedisConstants.CPU_CACHE_PAGE_LOCK+page;
        List cpuList=null;
        try {
            //获取失败，休眠
            if(!tryLock(lockKey)){
                Thread.sleep(50);
                getCpuList(page);
            }
            //获取成功
            //查数据库，页面条件 每次返回20个结构
            IPage resultPage = new Page(page, 20);
            LambdaQueryWrapper<Cpu> wrapper=new LambdaQueryWrapper<>();
            wrapper.select(Cpu::getCpuId,Cpu::getCpuName,Cpu::getCpuPrice,Cpu::getCpuImg);
            cpuDao.selectPage(resultPage, wrapper);
            //获取cpu列表
            cpuList = resultPage.getRecords();
            //如果返回的cpu列表是空值，将建立空白缓存(缓存穿透)
            if(cpuList.size()==0){
                stringRedisTemplate.opsForValue().set(RedisConstants.CPU_CACHE_PAGE_KEY + page, "",
                        RedisConstants.CACHE_NULL_TTL, TimeUnit.MINUTES);
                return null;
            }
            //获取总页数
            long cpuPageCount=resultPage.getPages();
            //转换json 存入redis 存活时间10min
            cpuListJson = JSONUtil.toJsonStr(cpuList);
            stringRedisTemplate.opsForValue().set(RedisConstants.CPU_CACHE_PAGE_KEY + page, cpuListJson,
                    RedisConstants.CACHE_PAGE_TTL, TimeUnit.MINUTES);
            //存页数
            stringRedisTemplate.opsForValue().set(RedisConstants.CPU_CACHE_PAGE_COUNT_KEY, Long.toString(cpuPageCount),
                    RedisConstants.CACHE_PAGE_TTL,TimeUnit.MINUTES);

        }catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            //释放互斥锁
            unlock(lockKey);
        }
        //返回数据
        return new Result(cpuList, null, null);

    }

    /**
     * 获取单个cpu
     * @param id cpu的id
     * @return cpu的信息
    */
    @Override
    public Result getCpu(Integer id) {
        //查Redis缓存
        String cpuJson = stringRedisTemplate.opsForValue().get(RedisConstants.CPU_CACHE_KEY + id);
        //非空（null / ""）存在返回
        if (StrUtil.isNotBlank(cpuJson)) {
            Cpu resultCpu = JSONUtil.toBean(cpuJson,Cpu.class);
            return new Result(resultCpu,null, null);
        }
        //如果是“”
        if(cpuJson !=null){
            return new Result(null,null, null);
        }
        //尝试获取锁
        String lockKey = RedisConstants.CPU_CACHE_LOCK+id;
        Cpu cpu=null;
        try {
            //获取失败，休眠
            if(!tryLock(lockKey)){
                Thread.sleep(50);
                getCpu(id);
            }
            //获取成功
            //查数据库 获取cpu
            cpu = cpuDao.selectById(id);
            //如果返回的cpu列表是空值，将建立空白缓存(缓存穿透)
            if(cpu==null){
                stringRedisTemplate.opsForValue().set(RedisConstants.CPU_CACHE_KEY + id, "",
                        RedisConstants.CACHE_NULL_TTL, TimeUnit.MINUTES);
                return null;
            }
            //转换json 存入redis 存活时间10min
            cpuJson = JSONUtil.toJsonStr(cpu);
            stringRedisTemplate.opsForValue().set(RedisConstants.CPU_CACHE_KEY + id, cpuJson,
                    RedisConstants.CACHE_TTL, TimeUnit.MINUTES);

        }catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            //释放互斥锁
            unlock(lockKey);
        }
        //返回数据
        return new Result(cpu, null, null);
    }

    /**
     * 返回页数
     *
     * @return 返回页数
     */
    @Override
    public Result getCpuPageCount() {
        //获取页数
        String pageCount = stringRedisTemplate.opsForValue().get(RedisConstants.CPU_CACHE_PAGE_COUNT_KEY);
        //重置时间
        stringRedisTemplate.expire(RedisConstants.CPU_CACHE_PAGE_COUNT_KEY,RedisConstants.CACHE_TTL, TimeUnit.MINUTES);
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
