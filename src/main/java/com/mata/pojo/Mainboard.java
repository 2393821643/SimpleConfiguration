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
@TableName("tb_mainboard")
public class Mainboard {
    @TableField(value = "mainboard_id")  //自定义映射
    @TableId(type = IdType.AUTO) //设置id生成策略
    private Integer mainboardId;
    @TableField(value = "mainboard_name")
    private String mainboardName;
    @TableField(value = "mainboard_size")
    private String mainboardSize;
    @TableField(value = "mainboard_brand")
    private String mainboardBrand;
    @TableField(value = "mainboard_price")
    private Integer mainboardPrice;
    @TableField(value = "mainboard_img")
    private String mainboardImg;
}
