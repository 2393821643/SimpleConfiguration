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
@TableName("tb_chassis")
public class Chassis {
    @TableField(value = "chassis_id")
    @TableId(type = IdType.AUTO)
    private Integer chassisId;
    @TableField(value = "chassis_name")
    private String chassisName;
    @TableField(value = "chassis_type")
    private String chassisType;
    @TableField(value = "chassis_brand")
    private String chassisBrand;
    @TableField(value = "chassis_price")
    private Integer chassisPrice;
    @TableField(value = "chassis_img")
    private String chassisImg;
}
