package com.tcy.smartai.service.vo;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;

@Data

@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatisticRespVo {

    Integer carCount = 0;

    Integer peopleCount = 0;

    String imageUrl;

    ArrayList<JSONObject> detail;
}
