package com.mata.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user_configuration ")
public class UserConfiguration {
    @Id
    private Object id;
    @Field("userId")
    private Integer userId;
    @Field("configurationName")
    private String configurationName;

    @Field("cpu")
    private Cpu cpu;
    @Field("cpuFan")
    private CpuFan cpuFan;
    @Field("gpu")
    private Gpu gpu;
    @Field("mainboard")
    private Mainboard mainboard;
    @Field("memoryList")
    private List<Memory> memoryList;
    @Field("hdList")
    private List<Hd> hdList;
    @Field("power")
    private Power power;
    @Field("chassis")
    private Chassis chassis;

}
