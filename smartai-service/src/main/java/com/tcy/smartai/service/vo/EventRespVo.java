package com.tcy.smartai.service.vo;

import lombok.Data;

import java.util.ArrayList;

@Data
public class EventRespVo {
    private String  eventCode;

    private String remark;

    private String eventUuid;

    private ArrayList images;

    private Long timestamp;

    private String cameraSn;
}
