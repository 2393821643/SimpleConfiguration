package com.mata.util;

public class RedisConstants {
    //验证码登录的键前缀
    public static final String LOGIN_CODE_KEY = "login:code:";

    //验证码登录的存活时间
    public static final Long LOGIN_CODE_TTL = 2L;

    //登录token的键前缀
    public static final String LOGIN_TOKEN_KEY = "login:token:";

    //登录token的存活时间
    public static final Long USER_TOKEN_TTL = 30L;

    //修改密码验证码的键前缀
    public static final String CHANGE_PASSWORD_CODE_KEY = "change:password:code:";

    //修改密码验证码的存活时间
    public static final Long CHANGE_PASSWORD_CODE_TTL = 2L;

    //缓存cpu页面的键前缀
    public static final String CPU_CACHE_PAGE_KEY="cpu:cache:page:";

    //缓存页面的存活时间
    public static final Long CACHE_PAGE_TTL = 10L;

    //缓存cpu页面锁的键前缀
    public static final String CPU_CACHE_PAGE_LOCK="cpu:lock:page:";

    //缓存锁存活时间
    public static final Long CACHE_LOCK_TTL = 5L;

    //空数据存活时间
    public static final Long CACHE_NULL_TTL= 2L;

    //缓存cpu信息的键前缀
    public static final String CPU_CACHE_KEY="cpu:cache:id:";

    //缓存硬件信息的存活时间
    public static final Long CACHE_TTL = 10L;

    //缓存cpu信息锁的键前缀
    public static final String CPU_CACHE_LOCK="cpu:lock:id:";

    //缓存gpu页面的键前缀
    public static final String GPU_CACHE_PAGE_KEY="gpu:cache:page:";

    //缓存gpu页面锁的键前缀
    public static final String GPU_CACHE_PAGE_LOCK="gpu:lock:page:";

    //缓存gpu信息的键前缀
    public static final String GPU_CACHE_KEY="gpu:cache:id:";

    //缓存Gpu信息锁的键前缀
    public static final String GPU_CACHE_LOCK="gpu:lock:id:";

    //缓存cpufan页面的键前缀
    public static final String CPUFAN_CACHE_PAGE_KEY="cpufan:cache:page:";

    //缓存cpufan页面锁的键前缀
    public static final String CPUFAN_CACHE_PAGE_LOCK="cpufan:lock:page:";

    //缓存cpufan信息的键前缀
    public static final String CPUFAN_CACHE_KEY="cpufan:cache:id:";

    //缓存cpufan信息锁的键前缀
    public static final String CPUFAN_CACHE_LOCK="cpufan:lock:id:";

    //缓存memory页面的键前缀
    public static final String MEMORY_CACHE_PAGE_KEY="memory:cache:page:";

    //缓存memory页面锁的键前缀
    public static final String MEMORY_CACHE_PAGE_LOCK="memory:lock:page:";

    //缓存memory信息的键前缀
    public static final String MEMORY_CACHE_KEY="memory:cache:id:";

    //缓存memory信息锁的键前缀
    public static final String MEMORY_CACHE_LOCK="memory:lock:id:";

    //缓存mainboard页面的键前缀
    public static final String MAINBOARD_CACHE_PAGE_KEY="mainboard:cache:page:";

    //缓存memory页面锁的键前缀
    public static final String MAINBOARD_CACHE_PAGE_LOCK="mainboard:lock:page:";

    //缓存memory信息的键前缀
    public static final String MAINBOARD_CACHE_KEY="mainboard:cache:id:";

    //缓存memory信息锁的键前缀
    public static final String MAINBOARD_CACHE_LOCK="mainboard:lock:id:";

    //缓存hd页面的键前缀
    public static final String HD_CACHE_PAGE_KEY="hd:cache:page:";

    //缓存hd页面锁的键前缀
    public static final String HD_CACHE_PAGE_LOCK="hd:lock:page:";

    //缓存hd信息的键前缀
    public static final String HD_CACHE_KEY="hd:cache:id:";

    //缓存hd信息锁的键前缀
    public static final String HD_CACHE_LOCK="hd:lock:id:";

    //缓存power页面的键前缀
    public static final String POWER_CACHE_PAGE_KEY="power:cache:page:";

    //缓存power页面锁的键前缀
    public static final String POWER_CACHE_PAGE_LOCK="power:lock:page:";

    //缓存power信息的键前缀
    public static final String POWER_CACHE_KEY="power:cache:id:";

    //缓存power信息锁的键前缀
    public static final String POWER_CACHE_LOCK="power:lock:id:";

    //缓存chassis页面的键前缀
    public static final String CHASSIS_CACHE_PAGE_KEY="chassis:cache:page:";

    //缓存power页面锁的键前缀
    public static final String CHASSIS_CACHE_PAGE_LOCK="chassis:lock:page:";

    //缓存power信息的键前缀
    public static final String CHASSIS_CACHE_KEY="chassis:cache:id:";

    //缓存power信息锁的键前缀
    public static final String CHASSIS_CACHE_LOCK="chassis:lock:id:";

    //缓存cpu总页数的键
    public static final String CPU_CACHE_PAGE_COUNT_KEY="cpu:cache:page:count";

    //缓存gpu总页数的键
    public static final String GPU_CACHE_PAGE_COUNT_KEY="gpu:cache:page:count";

    //缓存cpufan总页数的键
    public static final String CPUFAN_CACHE_PAGE_COUNT_KEY="cpufan:cache:page:count";

    //缓存memory总页数的键
    public static final String MEMORY_CACHE_PAGE_COUNT_KEY="memory:cache:page:count";

    //缓存mainboard总页数的键
    public static final String MAINBOARD_CACHE_PAGE_COUNT_KEY="mainboard:cache:page:count";

    //缓存hd总页数的键
    public static final String HD_CACHE_PAGE_COUNT_KEY="hd:cache:page:count";

    //缓存chassis总页数的键
    public static final String CHASSIS_CACHE_PAGE_COUNT_KEY="chassis:cache:page:count";

    //缓存power总页数的键
    public static final String POWER_CACHE_PAGE_COUNT_KEY="power:cache:page:count";

}
