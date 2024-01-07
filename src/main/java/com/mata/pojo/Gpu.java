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
@TableName("tb_gpu")
public class Gpu {
    @TableField(value = "gpu_id")  //自定义映射
    @TableId(type = IdType.AUTO) //设置id生成策略
    private Integer gpuId;
    @TableField(value = "gpu_name")
    private String gpuName;
    @TableField(value = "gpu_brand")
    private String gpuBrand;
    @TableField(value = "gpu_core")
    private Integer gpuCore;
    @TableField(value = "gpu_memory_size")
    private Integer gpuMemorySize;
    @TableField(value = "gpu_memory_type")
    private String gpuMemoryType;
    @TableField(value = "gpu_frequency")
    private Integer gpuFrequency;
    @TableField(value = "gpu_price")
    private Integer gpuPrice;
    @TableField(value = "gpu_img")
    private String gpuImg;
}
