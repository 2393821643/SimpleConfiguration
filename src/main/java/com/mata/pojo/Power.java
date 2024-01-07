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
@TableName("tb_power")
public class Power {
    @TableField(value = "power_id")
    @TableId(type = IdType.AUTO)
    private Integer powerId;
    @TableField(value = "power_name")
    private String powerName;
    @TableField(value = "power_size")
    private Integer powerSize;
    @TableField(value = "power_brand")
    private String powerBrand;
    @TableField(value = "power_price")
    private Integer powerPrice;
    @TableField(value = "power_img")
    private String powerImg;
}
