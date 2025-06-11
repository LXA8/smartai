package com.tcy.smartai.service.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.ArrayList;

@Data
public class DetectionVo {

    private String id;

    private String syncCode;

    private String cameraSn;

    JSONObject image;

    ArrayList<JSONObject> detectionResult;
}
