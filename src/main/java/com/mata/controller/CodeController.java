package com.mata.controller;

import com.mata.dto.Result;
import com.mata.service.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/code")
public class CodeController {
    @Autowired
    private CodeService codeService;

    //发送登录验证码
    @PostMapping("/login/{email}")
    public Result sendLoginCode(@PathVariable("email") String email) {
        return codeService.sendLoginCode(email);
    }

    //发送修改密码的邮箱验证码
    @PostMapping("/change-password/{email}")
    public Result sendChangePasswordCode(@PathVariable("email") String email){
        return codeService.sendChangePasswordCode(email);
    }
}
