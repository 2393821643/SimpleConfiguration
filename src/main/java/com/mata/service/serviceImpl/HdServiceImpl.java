package com.mata.service.serviceImpl;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mata.dao.HdDao;
import com.mata.dto.Result;
import com.mata.pojo.Hd;
import com.mata.service.HdService;
import com.mata.util.RedisConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class HdServiceImpl implements HdService {
    @Autowired
    private HdDao hdDao;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 获取hd列表
     * @param page 当前页
     * @return hdList/message
     */
    @Override
    public Result getHdList(Integer page) {
        //查Redis缓存
        String hdListJson = stringRedisTemplate.opsForValue().get(RedisConstants.HD_CACHE_PAGE_KEY + page);
        //非空（null / ""）存在返回
        if (StrUtil.isNotBlank(hdListJson)) {
            List<Hd> hdList = JSONUtil.toList(hdListJson, Hd.class);
            return new Result(hdList,null, null);
        }
        //如果是“”
        if(hdListJson !=null){
            return new Result(null,null, null);
        }
        //尝试获取锁
        String lockKey = RedisConstants.HD_CACHE_PAGE_LOCK+page;
        List hdList=null;
        try {
            //获取失败，休眠
            if(!tryLock(lockKey)){
                Thread.sleep(50);
                getHdList(page);
            }
            //获取成功
            //查数据库，页面条件 每次返回20个结构
            IPage resultPage = new Page(page, 20);
            LambdaQueryWrapper<Hd> wrapper=new LambdaQueryWrapper<>();
            wrapper.select(Hd::getHdId,Hd::getHdName,Hd::getHdPrice,Hd::getHdImg);
            hdDao.selectPage(resultPage, wrapper);
            //获取hd列表
            hdList = resultPage.getRecords();
            //如果返回的hd列表是空值，将建立空白缓存(缓存穿透)
            if(hdList.size()==0){
                stringRedisTemplate.opsForValue().set(RedisConstants.HD_CACHE_PAGE_KEY + page, "",
                        RedisConstants.CACHE_NULL_TTL, TimeUnit.MINUTES);
                return null;
            }
            //获取总页数
            long hdPageCount=resultPage.getPages();
            //转换json 存入redis 存活时间10min
            hdListJson = JSONUtil.toJsonStr(hdList);
            stringRedisTemplate.opsForValue().set(RedisConstants.HD_CACHE_PAGE_KEY + page, hdListJson,
                    RedisConstants.CACHE_PAGE_TTL, TimeUnit.MINUTES);
            //存页数
            stringRedisTemplate.opsForValue().set(RedisConstants.HD_CACHE_PAGE_COUNT_KEY, Long.toString(hdPageCount),
                    RedisConstants.CACHE_PAGE_TTL,TimeUnit.MINUTES);

        }catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            //释放互斥锁
            unlock(lockKey);
        }
        //返回数据
        return new Result(hdList, null, null);

    }

    /**
     * 获取单个hd
     * @param id hd的id
     * @return hd的信息
     */
    @Override
    public Result getHd(Integer id) {
        //查Redis缓存
        String hdJson = stringRedisTemplate.opsForValue().get(RedisConstants.HD_CACHE_KEY + id);
        //非空（null / ""）存在返回
        if (StrUtil.isNotBlank(hdJson)) {
            Hd resultHd = JSONUtil.toBean(hdJson,Hd.class);
            return new Result(resultHd,null, null);
        }
        //如果是“”
        if(hdJson !=null){
            return new Result(null,null, null);
        }
        //尝试获取锁
        String lockKey = RedisConstants.HD_CACHE_LOCK+id;
        Hd hd=null;
        try {
            //获取失败，休眠
            if(!tryLock(lockKey)){
                Thread.sleep(50);
                getHd(id);
            }
            //获取成功
            //查数据库 获取hd
            hd = hdDao.selectById(id);
            //如果返回的hd列表是空值，将建立空白缓存(缓存穿透)
            if(hd==null){
                stringRedisTemplate.opsForValue().set(RedisConstants.HD_CACHE_KEY + id, "",
                        RedisConstants.CACHE_NULL_TTL, TimeUnit.MINUTES);
                return null;
            }
            //转换json 存入redis 存活时间10min
            hdJson = JSONUtil.toJsonStr(hd);
            stringRedisTemplate.opsForValue().set(RedisConstants.HD_CACHE_KEY + id, hdJson,
                    RedisConstants.CACHE_TTL, TimeUnit.MINUTES);

        }catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            //释放互斥锁
            unlock(lockKey);
        }
        //返回数据
        return new Result(hd, null, null);
    }

    /**
     * 返回页数
     *
     * @return 返回页数
     */
    @Override
    public Result getHdPageCount() {
        //获取页数
        String pageCount = stringRedisTemplate.opsForValue().get(RedisConstants.HD_CACHE_PAGE_COUNT_KEY);
        //重置时间
        stringRedisTemplate.expire(RedisConstants.HD_CACHE_PAGE_COUNT_KEY,RedisConstants.CACHE_TTL, TimeUnit.MINUTES);
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
