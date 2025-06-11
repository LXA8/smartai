package com.tcy.smartai.service.entity;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document(collection = "config")
@Data
public class ConfigEntity {
    @Id// 必须指定id列
    private String id;

    @Field("event_url")
    private String eventUrl;

    @Field("async_resp_url")
    String asyncRespUrl;

    @Field("create_time")
    Date createTime;

    @Field("update_time")
    Date updateTime;

}
