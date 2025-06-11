package com.tcy.smartai.service.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;

@Data
public class EventVo {

    private String cameraSn;

    private String eventPicture;

    private String eventCode;

    private String eventDesc;

    private String needAlarm;

    private String graspCode;

    private String timeStamp;

}
