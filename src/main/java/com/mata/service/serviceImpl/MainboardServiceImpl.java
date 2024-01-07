package com.mata.service.serviceImpl;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mata.dao.MainboardDao;
import com.mata.dto.Result;
import com.mata.pojo.Mainboard;
import com.mata.service.MainboardService;
import com.mata.util.RedisConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class MainboardServiceImpl implements MainboardService {
    @Autowired
    private MainboardDao mainboardDao;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 获取mainboard列表
     * @param page 当前页
     * @return mainboardList/message
     */
    @Override
    public Result getMainboardList(Integer page) {
        //查Redis缓存
        String mainboardListJson = stringRedisTemplate.opsForValue().get(RedisConstants.MAINBOARD_CACHE_PAGE_KEY + page);
        //非空（null / ""）存在返回
        if (StrUtil.isNotBlank(mainboardListJson)) {
            List<Mainboard> mainboardList = JSONUtil.toList(mainboardListJson, Mainboard.class);
            return new Result(mainboardList,null, null);
        }
        //如果是“”
        if(mainboardListJson !=null){
            return new Result(null,null, null);
        }
        //尝试获取锁
        String lockKey = RedisConstants.MAINBOARD_CACHE_PAGE_LOCK+page;
        List mainboardList=null;
        try {
            //获取失败，休眠
            if(!tryLock(lockKey)){
                Thread.sleep(50);
                getMainboardList(page);
            }
            //获取成功
            //查数据库，页面条件 每次返回20个结构
            IPage resultPage = new Page(page, 20);
            LambdaQueryWrapper<Mainboard> wrapper=new LambdaQueryWrapper<>();
            wrapper.select(Mainboard::getMainboardId,Mainboard::getMainboardName,Mainboard::getMainboardPrice,Mainboard::getMainboardImg);
            mainboardDao.selectPage(resultPage, wrapper);
            //获取mainboard列表
            mainboardList = resultPage.getRecords();
            //如果返回的mainboard列表是空值，将建立空白缓存(缓存穿透)
            if(mainboardList.size()==0){
                stringRedisTemplate.opsForValue().set(RedisConstants.MAINBOARD_CACHE_PAGE_KEY + page, "",
                        RedisConstants.CACHE_NULL_TTL, TimeUnit.MINUTES);
                return null;
            }
            //获取总页数
            long mainboardPageCount=resultPage.getPages();
            //转换json 存入redis 存活时间10min
            mainboardListJson = JSONUtil.toJsonStr(mainboardList);
            stringRedisTemplate.opsForValue().set(RedisConstants.MAINBOARD_CACHE_PAGE_KEY + page, mainboardListJson,
                    RedisConstants.CACHE_PAGE_TTL, TimeUnit.MINUTES);
            //存页数
            stringRedisTemplate.opsForValue().set(RedisConstants.MAINBOARD_CACHE_PAGE_COUNT_KEY, Long.toString(mainboardPageCount),
                    RedisConstants.CACHE_PAGE_TTL,TimeUnit.MINUTES);

        }catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            //释放互斥锁
            unlock(lockKey);
        }
        //返回数据
        return new Result(mainboardList, null, null);

    }

    /**
     * 获取单个mainboard
     * @param id mainboard的id
     * @return mainboard的信息
     */
    @Override
    public Result getMainboard(Integer id) {
        //查Redis缓存
        String mainboardJson = stringRedisTemplate.opsForValue().get(RedisConstants.MAINBOARD_CACHE_KEY + id);
        //非空（null / ""）存在返回
        if (StrUtil.isNotBlank(mainboardJson)) {
            Mainboard resultMainboard = JSONUtil.toBean(mainboardJson,Mainboard.class);
            return new Result(resultMainboard,null, null);
        }
        //如果是“”
        if(mainboardJson !=null){
            return new Result(null,null, null);
        }
        //尝试获取锁
        String lockKey = RedisConstants.MAINBOARD_CACHE_LOCK+id;
        Mainboard mainboard=null;
        try {
            //获取失败，休眠
            if(!tryLock(lockKey)){
                Thread.sleep(50);
                getMainboard(id);
            }
            //获取成功
            //查数据库 获取mainboard
            mainboard = mainboardDao.selectById(id);
            //如果返回的mainboard列表是空值，将建立空白缓存(缓存穿透)
            if(mainboard==null){
                stringRedisTemplate.opsForValue().set(RedisConstants.MAINBOARD_CACHE_KEY + id, "",
                        RedisConstants.CACHE_NULL_TTL, TimeUnit.MINUTES);
                return null;
            }
            //转换json 存入redis 存活时间10min
            mainboardJson = JSONUtil.toJsonStr(mainboard);
            stringRedisTemplate.opsForValue().set(RedisConstants.MAINBOARD_CACHE_KEY + id, mainboardJson,
                    RedisConstants.CACHE_TTL, TimeUnit.MINUTES);

        }catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            //释放互斥锁
            unlock(lockKey);
        }
        //返回数据
        return new Result(mainboard, null, null);
    }

    /**
     * 返回页数
     *
     * @return 返回页数
     */
    @Override
    public Result getMainboardPageCount() {
        //获取页数
        String pageCount = stringRedisTemplate.opsForValue().get(RedisConstants.MAINBOARD_CACHE_PAGE_COUNT_KEY);
        //重置时间
        stringRedisTemplate.expire(RedisConstants.MAINBOARD_CACHE_PAGE_COUNT_KEY,RedisConstants.CACHE_TTL, TimeUnit.MINUTES);
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
