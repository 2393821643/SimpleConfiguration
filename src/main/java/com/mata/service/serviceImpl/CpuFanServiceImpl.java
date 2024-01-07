package com.mata.service.serviceImpl;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mata.dao.CpuFanDao;
import com.mata.dao.GpuDao;
import com.mata.dto.Result;
import com.mata.pojo.CpuFan;
import com.mata.pojo.Gpu;
import com.mata.service.CpuFanService;
import com.mata.service.GpuService;
import com.mata.util.RedisConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class CpuFanServiceImpl implements CpuFanService {
    @Autowired
    private CpuFanDao cpufanDao;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 获取cpufan列表
     * @param page 当前页
     * @return cpufanList/message
     */
    @Override
    public Result getCpuFanList(Integer page) {
        //查Redis缓存
        String cpuFanListJson = stringRedisTemplate.opsForValue().get(RedisConstants.CPUFAN_CACHE_PAGE_KEY + page);
        //非空（null / ""）存在返回
        if (StrUtil.isNotBlank(cpuFanListJson)) {
            List<CpuFan> cpuFanList = JSONUtil.toList(cpuFanListJson, CpuFan.class);
            return new Result(cpuFanList,null, null);
        }
        //如果是“”
        if(cpuFanListJson !=null){
            return new Result(null,null, null);
        }
        //尝试获取锁
        String lockKey = RedisConstants.CPUFAN_CACHE_PAGE_LOCK+page;
        List cpuFanList=null;
        try {
            //获取失败，休眠
            if(!tryLock(lockKey)){
                Thread.sleep(50);
                getCpuFanList(page);
            }
            //获取成功
            //查数据库，页面条件 每次返回20个结构
            IPage resultPage = new Page(page, 20);
            LambdaQueryWrapper<CpuFan> wrapper=new LambdaQueryWrapper<>();
            wrapper.select(CpuFan::getCpuFanId,CpuFan::getCpuFanName,CpuFan::getCpuFanPrice,CpuFan::getCpuFanImg);
            cpufanDao.selectPage(resultPage, wrapper);
            //获取cpufan列表
            cpuFanList = resultPage.getRecords();
            //如果返回的cpufan列表是空值，将建立空白缓存(缓存穿透)
            if(cpuFanList.size()==0){
                stringRedisTemplate.opsForValue().set(RedisConstants.CPUFAN_CACHE_PAGE_KEY + page, "",
                        RedisConstants.CACHE_NULL_TTL, TimeUnit.MINUTES);
                return null;
            }
            //获取总页数
            long cpuFanPageCount=resultPage.getPages();
            //转换json 存入redis 存活时间10min
            cpuFanListJson = JSONUtil.toJsonStr(cpuFanList);
            stringRedisTemplate.opsForValue().set(RedisConstants.CPUFAN_CACHE_PAGE_KEY + page, cpuFanListJson,
                    RedisConstants.CACHE_PAGE_TTL, TimeUnit.MINUTES);
            //存页数
            stringRedisTemplate.opsForValue().set(RedisConstants.CPUFAN_CACHE_PAGE_COUNT_KEY, Long.toString(cpuFanPageCount),
                    RedisConstants.CACHE_PAGE_TTL,TimeUnit.MINUTES);

        }catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            //释放互斥锁
            unlock(lockKey);
        }
        //返回数据
        return new Result(cpuFanList, null, null);

    }

    /**
     * 获取单个cpufan
     * @param id cpufan的id
     * @return cpufan的信息
     */
    @Override
    public Result getCpuFan(Integer id) {
        //查Redis缓存
        String cpuFanJson = stringRedisTemplate.opsForValue().get(RedisConstants.CPUFAN_CACHE_KEY + id);
        //非空（null / ""）存在返回
        if (StrUtil.isNotBlank(cpuFanJson)) {
            CpuFan resultCpufan = JSONUtil.toBean(cpuFanJson,CpuFan.class);
            return new Result(resultCpufan,null, null);
        }
        //如果是“”
        if(cpuFanJson !=null){
            return new Result(null,null, null);
        }
        //尝试获取锁
        String lockKey = RedisConstants.CPUFAN_CACHE_LOCK+id;
        CpuFan cpuFan=null;
        try {
            //获取失败，休眠
            if(!tryLock(lockKey)){
                Thread.sleep(50);
                getCpuFan(id);
            }
            //获取成功
            //查数据库 获取cpufan
            cpuFan = cpufanDao.selectById(id);
            //如果返回的cpufan列表是空值，将建立空白缓存(缓存穿透)
            if(cpuFan==null){
                stringRedisTemplate.opsForValue().set(RedisConstants.CPUFAN_CACHE_KEY + id, "",
                        RedisConstants.CACHE_NULL_TTL, TimeUnit.MINUTES);
                return null;
            }
            //转换json 存入redis 存活时间10min
            cpuFanJson = JSONUtil.toJsonStr(cpuFan);
            stringRedisTemplate.opsForValue().set(RedisConstants.CPUFAN_CACHE_KEY + id, cpuFanJson,
                    RedisConstants.CACHE_TTL, TimeUnit.MINUTES);

        }catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            //释放互斥锁
            unlock(lockKey);
        }
        //返回数据
        return new Result(cpuFan, null, null);
    }

    /**
     * 返回页数
     *
     * @return 返回页数
     */
    @Override
    public Result getCpuFanPageCount() {
        //获取页数
        String pageCount = stringRedisTemplate.opsForValue().get(RedisConstants.CPUFAN_CACHE_PAGE_COUNT_KEY);
        //重置时间
        stringRedisTemplate.expire(RedisConstants.CPUFAN_CACHE_PAGE_COUNT_KEY,RedisConstants.CACHE_TTL, TimeUnit.MINUTES);
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
