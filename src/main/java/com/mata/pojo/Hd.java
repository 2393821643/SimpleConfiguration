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
@TableName("tb_hd")
public class Hd {
    @TableField(value = "hd_id")
    @TableId(type = IdType.AUTO)
    private Integer hdId;
    @TableField(value = "hd_name")
    private String hdName;
    @TableField(value = "hd_brand")
    private String hdBrand;
    @TableField(value = "hd_type")
    private String hdType;
    @TableField(value = "hd_base")
    private String hdBase;
    @TableField(value = "hd_size")
    private String hdSize;
    @TableField(value = "hd_price")
    private Integer hdPrice;
    @TableField(value = "hd_img")
    private String hdImg;
}
