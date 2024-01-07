package com.mata.service.serviceImpl;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mata.dao.MemoryDao;
import com.mata.dto.Result;
import com.mata.pojo.Memory;
import com.mata.service.MemoryService;
import com.mata.util.RedisConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class MemoryServiceImpl implements MemoryService {
    @Autowired
    private MemoryDao memoryDao;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 获取memory列表
     *
     * @param page 当前页
     * @return memoryList/message
     */
    @Override
    public Result getMemoryList(Integer page) {
        //查Redis缓存
        String memoryListJson = stringRedisTemplate.opsForValue().get(RedisConstants.MEMORY_CACHE_PAGE_KEY + page);
        //非空（null / ""）存在返回
        if (StrUtil.isNotBlank(memoryListJson)) {
            List<Memory> memoryList = JSONUtil.toList(memoryListJson, Memory.class);
            return new Result(memoryList, null, null);
        }
        //如果是“”
        if (memoryListJson != null) {
            return new Result(null, null, null);
        }
        //尝试获取锁
        String lockKey = RedisConstants.MEMORY_CACHE_PAGE_LOCK + page;
        List memoryList = null;
        try {
            //获取失败，休眠
            if (!tryLock(lockKey)) {
                Thread.sleep(50);
                getMemoryList(page);
            }
            //获取成功
            //查数据库，页面条件 每次返回20个结构
            IPage resultPage = new Page(page, 20);
            LambdaQueryWrapper<Memory> wrapper = new LambdaQueryWrapper<>();
            wrapper.select(Memory::getMemoryId, Memory::getMemoryName, Memory::getMemoryPrice, Memory::getMemoryImg);
            memoryDao.selectPage(resultPage, wrapper);
            //获取memory列表
            memoryList = resultPage.getRecords();
            //如果返回的memory列表是空值，将建立空白缓存(缓存穿透)
            if (memoryList.size() == 0) {
                stringRedisTemplate.opsForValue().set(RedisConstants.MEMORY_CACHE_PAGE_KEY + page, "",
                        RedisConstants.CACHE_NULL_TTL, TimeUnit.MINUTES);
                return null;
            }
            //获取总页数
            long memoryPageCount=resultPage.getPages();
            //转换json 存入redis 存活时间10min
            memoryListJson = JSONUtil.toJsonStr(memoryList);
            stringRedisTemplate.opsForValue().set(RedisConstants.MEMORY_CACHE_PAGE_KEY + page, memoryListJson,
                    RedisConstants.CACHE_PAGE_TTL, TimeUnit.MINUTES);
            //存页数
            stringRedisTemplate.opsForValue().set(RedisConstants.MEMORY_CACHE_PAGE_COUNT_KEY, Long.toString(memoryPageCount),
                    RedisConstants.CACHE_PAGE_TTL,TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            //释放互斥锁
            unlock(lockKey);
        }
        //返回数据
        return new Result(memoryList, null, null);

    }

    /**
     * 获取单个memory
     *
     * @param id memory的id
     * @return memory的信息
     */
    @Override
    public Result getMemory(Integer id) {
        //查Redis缓存
        String memoryJson = stringRedisTemplate.opsForValue().get(RedisConstants.MEMORY_CACHE_KEY + id);
        //非空（null / ""）存在返回
        if (StrUtil.isNotBlank(memoryJson)) {
            Memory resultMemory = JSONUtil.toBean(memoryJson, Memory.class);
            return new Result(resultMemory, null, null);
        }
        //如果是“”
        if (memoryJson != null) {
            return new Result(null, null, null);
        }
        //尝试获取锁
        String lockKey = RedisConstants.MEMORY_CACHE_LOCK + id;
        Memory memory = null;
        try {
            //获取失败，休眠
            if (!tryLock(lockKey)) {
                Thread.sleep(50);
                getMemory(id);
            }
            //获取成功
            //查数据库 获取memory
            memory = memoryDao.selectById(id);
            //如果返回的memory列表是空值，将建立空白缓存(缓存穿透)
            if (memory == null) {
                stringRedisTemplate.opsForValue().set(RedisConstants.MEMORY_CACHE_KEY + id, "",
                        RedisConstants.CACHE_NULL_TTL, TimeUnit.MINUTES);
                return null;
            }
            //转换json 存入redis 存活时间10min
            memoryJson = JSONUtil.toJsonStr(memory);
            stringRedisTemplate.opsForValue().set(RedisConstants.MEMORY_CACHE_KEY + id, memoryJson,
                    RedisConstants.CACHE_TTL, TimeUnit.MINUTES);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            //释放互斥锁
            unlock(lockKey);
        }
        //返回数据
        return new Result(memory, null, null);
    }


    /**
     * 返回页数
     *
     * @return 返回页数
     */
    @Override
    public Result getMemoryPageCount() {
        //获取页数
        String pageCount = stringRedisTemplate.opsForValue().get(RedisConstants.MEMORY_CACHE_PAGE_COUNT_KEY);
        //重置时间
        stringRedisTemplate.expire(RedisConstants.MEMORY_CACHE_PAGE_COUNT_KEY, RedisConstants.CACHE_TTL, TimeUnit.MINUTES);
        return new Result(Integer.parseInt(pageCount), null, null);
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
