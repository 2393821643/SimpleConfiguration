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
@TableName("tb_memory")
public class Memory {
    @TableField(value = "memory_id")
    @TableId(type = IdType.AUTO)
    private Integer memoryId;
    @TableField(value = "memory_name")
    private String memoryName;
    @TableField(value = "memory_type")
    private String memoryType;
    @TableField(value = "memory_size")
    private Integer memorySize;
    @TableField(value = "memory_frequency")
    private Integer memoryFrequency;
    @TableField(value = "memory_brand")
    private String memoryBrand;
    @TableField(value = "memory_price")
    private Integer memoryPrice;
    @TableField(value = "memory_img")
    private String memoryImg;
}
