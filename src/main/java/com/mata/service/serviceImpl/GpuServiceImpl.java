package com.mata.service.serviceImpl;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mata.dao.GpuDao;
import com.mata.dto.Result;
import com.mata.pojo.Gpu;
import com.mata.service.GpuService;
import com.mata.util.RedisConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class GpuServiceImpl implements GpuService {
    @Autowired
    private GpuDao gpuDao;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    /**
     * 获取gpu列表
     * @param page 当前页
     * @return gpuList/message
    */
    @Override
    public Result getGpuList(Integer page) {
        //查Redis缓存
        String gpuListJson = stringRedisTemplate.opsForValue().get(RedisConstants.GPU_CACHE_PAGE_KEY + page);
        //非空（null / ""）存在返回
        if (StrUtil.isNotBlank(gpuListJson)) {
            List<Gpu> gpuList = JSONUtil.toList(gpuListJson, Gpu.class);
            return new Result(gpuList,null, null);
        }
        //如果是“”
        if(gpuListJson !=null){
            return new Result(null,null, null);
        }
        //尝试获取锁
        String lockKey = RedisConstants.GPU_CACHE_PAGE_LOCK+page;
        List gpuList=null;
        try {
            //获取失败，休眠
            if(!tryLock(lockKey)){
                Thread.sleep(50);
                getGpuList(page);
            }
            //获取成功
            //查数据库，页面条件 每次返回20个结构
            IPage resultPage = new Page(page, 20);
            LambdaQueryWrapper<Gpu> wrapper=new LambdaQueryWrapper<>();
            wrapper.select(Gpu::getGpuId,Gpu::getGpuName,Gpu::getGpuPrice,Gpu::getGpuImg);
            gpuDao.selectPage(resultPage, wrapper);
            //获取gpu列表
            gpuList = resultPage.getRecords();
            //如果返回的gpu列表是空值，将建立空白缓存(缓存穿透)
            if(gpuList.size()==0){
                stringRedisTemplate.opsForValue().set(RedisConstants.GPU_CACHE_PAGE_KEY + page, "",
                        RedisConstants.CACHE_NULL_TTL, TimeUnit.MINUTES);
                return null;
            }
            //获取总页数
            long gpuPageCount=resultPage.getPages();
            //转换json 存入redis 存活时间10min
            gpuListJson = JSONUtil.toJsonStr(gpuList);
            stringRedisTemplate.opsForValue().set(RedisConstants.GPU_CACHE_PAGE_KEY + page, gpuListJson,
                    RedisConstants.CACHE_PAGE_TTL, TimeUnit.MINUTES);
            //存页数
            stringRedisTemplate.opsForValue().set(RedisConstants.GPU_CACHE_PAGE_COUNT_KEY, Long.toString(gpuPageCount),
                    RedisConstants.CACHE_PAGE_TTL,TimeUnit.MINUTES);

        }catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            //释放互斥锁
            unlock(lockKey);
        }
        //返回数据
        return new Result(gpuList, null, null);

    }

    /**
     * 获取单个gpu
     * @param id gpu的id
     * @return gpu的信息
    */
    @Override
    public Result getGpu(Integer id) {
        //查Redis缓存
        String gpuJson = stringRedisTemplate.opsForValue().get(RedisConstants.GPU_CACHE_KEY + id);
        //非空（null / ""）存在返回
        if (StrUtil.isNotBlank(gpuJson)) {
            Gpu resultGpu = JSONUtil.toBean(gpuJson,Gpu.class);
            return new Result(resultGpu,null, null);
        }
        //如果是“”
        if(gpuJson !=null){
            return new Result(null,null, null);
        }
        //尝试获取锁
        String lockKey = RedisConstants.GPU_CACHE_LOCK+id;
        Gpu gpu=null;
        try {
            //获取失败，休眠
            if(!tryLock(lockKey)){
                Thread.sleep(50);
                getGpu(id);
            }
            //获取成功
            //查数据库 获取gpu
            gpu = gpuDao.selectById(id);
            //如果返回的gpu列表是空值，将建立空白缓存(缓存穿透)
            if(gpu==null){
                stringRedisTemplate.opsForValue().set(RedisConstants.GPU_CACHE_KEY + id, "",
                        RedisConstants.CACHE_NULL_TTL, TimeUnit.MINUTES);
                return null;
            }
            //转换json 存入redis 存活时间10min
            gpuJson = JSONUtil.toJsonStr(gpu);
            stringRedisTemplate.opsForValue().set(RedisConstants.GPU_CACHE_KEY + id, gpuJson,
                    RedisConstants.CACHE_TTL, TimeUnit.MINUTES);

        }catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            //释放互斥锁
            unlock(lockKey);
        }
        //返回数据
        return new Result(gpu, null, null);
    }

    /**
     * 返回页数
     *
     * @return 返回页数
     */
    @Override
    public Result getGpuPageCount() {
        //获取页数
        String pageCount = stringRedisTemplate.opsForValue().get(RedisConstants.GPU_CACHE_PAGE_COUNT_KEY);
        //重置时间
        stringRedisTemplate.expire(RedisConstants.GPU_CACHE_PAGE_COUNT_KEY,RedisConstants.CACHE_TTL, TimeUnit.MINUTES);
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
