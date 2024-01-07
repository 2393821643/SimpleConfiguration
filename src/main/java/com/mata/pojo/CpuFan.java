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
@TableName("tb_cpu_fan")
public class CpuFan {
    @TableField(value = "cpu_fan_id")
    @TableId(type = IdType.AUTO)
    private Integer cpuFanId;
    @TableField(value = "cpu_fan_name")
    private String cpuFanName;
    @TableField(value = "cpu_fan_brand")
    private String cpuFanBrand;
    @TableField(value = "cpu_fan_price")
    private Integer cpuFanPrice;
    @TableField(value = "cpu_fan_img")
    private String cpuFanImg;
}
