package com.mata.service;

import com.mata.dto.Result;

public interface CodeService {
    //发送登录验证码
    public Result sendLoginCode(String email);

    //发送修改密码的邮箱验证码
    public Result sendChangePasswordCode(String email);
}
