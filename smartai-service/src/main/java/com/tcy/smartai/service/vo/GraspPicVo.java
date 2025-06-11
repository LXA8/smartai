package com.tcy.smartai.service.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.ArrayList;

@Data
public class GraspPicVo {
    private String cameraSn;

    private String graspCode;

    ArrayList<JSONObject> graspPictures;

}
