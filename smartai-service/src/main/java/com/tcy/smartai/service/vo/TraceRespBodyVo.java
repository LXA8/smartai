package com.tcy.smartai.service.vo;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tcy.smartai.service.entity.TraceResultListEntity;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TraceRespBodyVo {
    String responseType;

    String requestUuid;

    TraceResultListEntity data;

    int code;

    String msg;
}
