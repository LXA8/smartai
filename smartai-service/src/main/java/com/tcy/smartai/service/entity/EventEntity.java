package com.tcy.smartai.service.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.Date;

@Document(collection = "event")
@Data
public class EventEntity {
    @Id// 必须指定id列
    private String id;

    @Field("camera_sn")
    private String cameraSn;

    @Field("event_picture")
    private String eventPicture;

    @Field("event_code")
    private String eventCode;

    @Field("event_desc")
    private String eventDesc;

    @Field("need_alarm")
    private String needAlarm;

    @Field("grasp_code")
    private String graspCode;

    @Field("time_stamp")
    private Date timeStamp;

    @Field("create_time")
    private Date createTime;

    @Field("update_time")
    private Date updateTime;

    @Field("grasp_pictures")
    ArrayList graspPictures;
}
