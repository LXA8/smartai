package com.tcy.smartai.service.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
@Document(collection = "neteq")
public class NetEqEntity {
    @Id// 必须指定id列
    private String id;

    @Field("neteq_id")
    String netEqID;

    @Field("desc")
    String desc;

    @Field("create_time")
     Date createTime;

    @Field("update_time")
    private Date updateTime;

    @Field("heartbeat_time")
    private Date heartbeatTime;

    @Field("camera_rec_version")
    int cameraRecVersion;

    @Field("camera_total")
    int cameraTotal;


}
