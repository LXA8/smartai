package com.tcy.smartai.service.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.Map;

@Data
@Document(collection = "statistics")
public class StatisticEntity {

    @Id// 必须指定id列
    private String id;

    @Field("camera_sn")
    String  cameraSn;

    @Field("statistics_data")
    Map data;

    @Field("statistics_time")
    Date statisticsTime;

    @Field("create_time")
    Date createTime;
}
