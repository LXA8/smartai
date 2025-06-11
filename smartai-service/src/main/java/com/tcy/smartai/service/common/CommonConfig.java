package com.tcy.smartai.service.common;

public class CommonConfig {

    //统计数据
    public  static final int DETECT_PEOPLE_TYPE = 1;
    public  static final int DETECT_CAR_TYPE = 2;
    public  static final int DETECT_STALLKEEPER_TYPE = 3;
    public  static final int DETECT_NONMOTOR_TYPE = 4;

    public  static final String DETECT_PEOPLE_NUM = "peopleNum";
    public  static final String DETECT_CAR_NUM = "carNum";
    public  static final String DETECT_CAR_COUNT = "carCount";
    public  static final String DETECT_PEOPLE_COUNT = "peopleCount";
    public  static final String DETECT_HOUR = "hour";

    public  static final String DETECT_TYPE = "type";
    public  static final String DETECT_OBJECTCOUNT = "objectCount";

    //事件
    public  static final String EVENT_CODE_E001 = "人流拥堵";
    public  static final String EVENT_CODE_E002 = "占道设摊/违规搭建";
    public  static final String EVENT_CODE_E003 = "非机动车违停";
    public  static final String EVENT_CODE_E004 = "机动车违章";

    public  static final String EVENT_URL = "eventUrl";

    public  static final String ASYNC_RESP_URL = "asyncRespUrl";

    public static final String UPLOAD_PICTURE_PATH = "uploadPicturePath";

    public static final String FILE_PATH = "filePath";

    public  static String getEventName(String eventCode){
        if("E001".equals(eventCode)){
            return EVENT_CODE_E001;
        }else if("E002".equals(eventCode)){
            return EVENT_CODE_E002;
        }else if("E003".equals(eventCode)){
            return EVENT_CODE_E003;
        }else if("E004".equals(eventCode)){
            return EVENT_CODE_E004;
        }else{
            return null;
        }
    }

}
