package com.tcy.smartai.service.enums;

public enum ErrorCodeEnum {
    SUCCESS(200,"success"),
    SERVER_CODE_ERROR(1000,"server code error"),
    ILLEGAL_ARGUMENT_INAVLID(1001,"invalid parameter"),

    //配置
    CONFIG_ERROR(1010,"config error"),

    //摄像头
    CAMERAS_SN_NULL(1020,"camera sn is null"),
    CAMERAS_SN_EXIST(1021,"camera has exist"),
    CAMERAS_ALLOCATION_FAIL(1022,"allocat neteq for camera failed"),
    CAMERAS_SN_NOT_EXIST(1023,"camera has not exist"),



    //事件
    EVENT_SN_NULL(1030,"camera sn or grasp code is null"),
    EVENT_PARAM_NOT_OK(1031,"camera sn or grasp code or grasppic list is null"),
    EVENT_PARAM_GRASP_NULL(1032,"grasp code is null"),
    EVENT_HAS_EXIST(1033,"event has exist"),

    //检测
    DETECTION_IMAGE_CAMERA_NULL(1040,"image  or camera is null"),

    //网元
    NETEQ_ID_NULL(1050,"neteq id is null"),
    NETEQ_AVALID_NULL(1051,"have not avalid neteq"),

    //追踪
    TRACE_AVALID_NULL(1060,"param is null"),
    TRACE_RESPONSE_NULL(1061,"response is null");

    private final  Integer code;
    private final String desc;
    ErrorCodeEnum(Integer code,String desc){
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode(){
        return code;
    }

    public String getDesc(){
        return desc;
    }

}
