package com.tcy.smartai.service.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.ArrayList;

@Data
public class CameraVo {

    String  cameraSn;

    String address;

    String lngLat;

    String streamUrl;

    ArrayList<JSONObject> algorithms;
}
