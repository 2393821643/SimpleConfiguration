package com.mata.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_user")
public class User {
    @TableField(value = "user_id")  //自定义映射
    @TableId(type = IdType.AUTO) //设置id生成策略
    private Integer userId;
    @TableField(value = "user_email")
    private String userEmail;
    @TableField(value = "user_password")
    private String userPassword;
    @TableField(value = "username")
    private String username;
}
