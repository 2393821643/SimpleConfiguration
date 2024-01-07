package com.mata.service.serviceImpl;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.RandomUtil;
import com.mata.dto.Code;
import com.mata.dto.Result;
import com.mata.service.CodeService;
import com.mata.util.EmailMessage;
import com.mata.util.RedisConstants;
import com.mata.util.SendEmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


@Service
public class CodeServiceImpl implements CodeService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 发送邮箱登录验证码
     *
     * @param email 邮箱
     * @return message
     */
    @Override
    public Result sendLoginCode(String email) {
        if(email.length()>30){
            return new Result(null, Code.EMAIL_ERR, "邮箱格式错误");
        }
        //判断是不是email
        boolean isEmail = Validator.isEmail(email);
        if (!isEmail) {
            return new Result(null, Code.EMAIL_ERR, "邮箱格式错误");
        } else {
            //设置随机6位验证码
            String code = RandomUtil.randomNumbers(6);
            //发送邮箱
            SendEmailUtil.sendMail(email,
                    EmailMessage.SEND_LOGIN_CODE_MESSAGE_FOREBODY + code + EmailMessage.SEND_LOGIN_CODE_MESSAGE_BEHINDBODY,
                    "SimpleConfiguration");
            //存入Redis 存活时间2min
            stringRedisTemplate.opsForValue().set(RedisConstants.LOGIN_CODE_KEY + email, code,
                    RedisConstants.LOGIN_CODE_TTL, TimeUnit.MINUTES);
            return new Result(null, Code.SEND_EMAIL_OK, "发送成功");
        }
    }

    /**
     * 发送邮箱修改密码验证码
     *
     * @param email 邮箱
     * @return message
     */
    @Override
    public Result sendChangePasswordCode(String email) {
        //判断是不是email
        boolean isEmail = Validator.isEmail(email);
        if (!isEmail) {
            return new Result(null, Code.EMAIL_ERR, "邮箱格式错误");
        } else {
            //设置随机6位验证码
            String code = RandomUtil.randomNumbers(6);
            //发送邮箱
            SendEmailUtil.sendMail(email,
                    EmailMessage.SEND_CHANGE_PASSWORD_CODE_MESSAGE_FOREBODY + code + EmailMessage.SEND_CHANGE_PASSWORD_CODE_MESSAGE_BEHINDBODY,
                    "SimpleConfiguration");
            //存入Redis 存活时间2min
            stringRedisTemplate.opsForValue().set(RedisConstants.CHANGE_PASSWORD_CODE_KEY + email, code,
                    RedisConstants.CHANGE_PASSWORD_CODE_TTL, TimeUnit.MINUTES);
            return new Result(null, Code.SEND_EMAIL_OK, "发送成功");
        }
    }


}
