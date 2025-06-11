package com.tcy.smartai.service.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.Date;

@Document(collection = "cameras")
@Data
public class CamerasEntity {

    @Id// 必须指定id列
    private String id;

    @Field("camera_sn")
    String  cameraSn;

    @Field("address")
    String address;

    @Field("lng_lat")
    String lngLat;

    @Field("stream_url")
    String streamUrl;

    @Field("algorithms")
    ArrayList algorithms;

    @Field("create_time")
    Date createTime;

    @Field("update_time")
    Date updateTime;

    @Field("is_delete")
    Boolean isDelete;

    @Field("neteq_id")
    String netEqID;
}
