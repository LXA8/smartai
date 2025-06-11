package com.tcy.smartai.service.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GraspPicRespVo {
    String op;

    String path;

    List value;
}
