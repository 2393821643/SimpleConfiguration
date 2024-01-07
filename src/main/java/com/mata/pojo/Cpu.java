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
@TableName("tb_cpu")
public class Cpu {
    @TableField(value = "cpu_id")  //自定义映射
    @TableId(type = IdType.AUTO) //设置id生成策略
    private Integer cpuId;
    @TableField(value = "cpu_name")
    private String cpuName;
    @TableField(value = "cpu_base")
    private String cpuBase;
    @TableField(value = "cpu_brand")
    private String cpuBrand;
    @TableField(value = "cpu_core")
    private Integer cpuCore;
    @TableField(value = "cpu_thread")
    private Integer cpuThread;
    @TableField(value = "cpu_frequency")
    private Double cpuFrequency;
    @TableField(value = "cpu_price")
    private Integer cpuPrice;
    @TableField(value = "cpu_img")
    private String cpuImg;


}
