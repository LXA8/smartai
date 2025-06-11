package com.tcy.smartai.service.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.Date;

@Document(collection = "detection")
@Data
public class DetectionEvent {
    @Id// 必须指定id列
    private String id;

    @Field("sync_code")
    private String syncCode;

    @Field("camera_sn")
    private String cameraSn;

    @Field("image")
    JSONObject image;

    @Field("detection_result")
    ArrayList<JSONObject> detectionResult;

    @Field("time_stamp")
    private Date timeStamp;

    @Field("create_time")
    private Date createTime;

}
