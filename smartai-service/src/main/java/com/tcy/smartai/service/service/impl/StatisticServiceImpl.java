package com.tcy.smartai.service.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tcy.smartai.service.common.CommonConfig;
import com.tcy.smartai.service.common.Utils;
import com.tcy.smartai.service.dao.DetectionDao;
import com.tcy.smartai.service.dao.StatisticsDao;
import com.tcy.smartai.service.entity.DetectionEvent;
import com.tcy.smartai.service.entity.StatisticEntity;
import com.tcy.smartai.service.enums.ErrorCodeEnum;
import com.tcy.smartai.service.exception.BusinessException;
import com.tcy.smartai.service.service.StatisticService;
import com.tcy.smartai.service.vo.StatisticRespVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.util.*;

@Service
public class StatisticServiceImpl implements StatisticService {

    public static Logger logger = LoggerFactory.getLogger(StatisticServiceImpl.class);

    @Autowired
    StatisticsDao statisticsDao;

    @Autowired
    DetectionDao detectionDao;

    @Value("${path.url.image}")
    private String pathUrl;


    private static String MIN_PRE ="min_";

    private static String MAX_PRE ="max_";

    @Scheduled(cron = "0 1 * * * ? ")
    public void scheduleDetecteData(){
       //获取前一小时的时间范围
        String preHourTimeStr = Utils.getPreHourTime();
        String startPreHourTimeStr = preHourTimeStr+"0000";
        String endPreHourTimeStr = preHourTimeStr+"5959";
        logger.info("开始统计数据,start:{},end:{}",startPreHourTimeStr,endPreHourTimeStr);
        try{
            //拼装该时间段所有的数据
            List<StatisticEntity> entities = statisticForHour(startPreHourTimeStr,endPreHourTimeStr,null);
            //批量写入
            logger.debug("批量写入统计值:{}",JSON.toJSONString(entities));
            if(entities == null || entities.size()==0){
                logger.info("待批量需要写入统计值为空，结束本次统计");
                return;
            }
            statisticsDao.batchInsert(entities);
        }catch (Exception e){
            logger.error("当前时间段:{},检查数据统计失败，msg:{}",preHourTimeStr,e);
        }
    }

    private List<StatisticEntity> statisticForHour(String startPreHourTimeStr,String endPreHourTimeStr,String currentCameraSn) throws ParseException {
        Date starPreHourtTime = Utils.strToDate(startPreHourTimeStr);
        Date endPreHourTime = Utils.strToDate(endPreHourTimeStr);
        List<DetectionEvent> events = null;
        if(StringUtils.isEmpty(currentCameraSn)){
            events = detectionDao.findOneHourAllDetecton(starPreHourtTime,endPreHourTime);
        }else{
            events = detectionDao.findOneHourAllDetectonByCameraSn(starPreHourtTime,endPreHourTime,currentCameraSn);
        }
        if(events ==  null || events.size() ==0){
            logger.info("当前时间段需要统计的数据为空");
            return null;
        }
        Map staticMap = new HashMap<String,Map<String,Integer>>();
        //对于一个小时的数据进行计算，按照type筛选出最大值，最小值
        for(DetectionEvent event:events){
            String cameraSn = event.getCameraSn();
            ArrayList<JSONObject> detections = event.getDetectionResult();
            if(detections == null){
                logger.info("摄像头:{},没有探测数据，跳过统计",cameraSn);
                continue;
            }
            Map<String,Integer> cameraMap = (Map<String,Integer>)staticMap.get(cameraSn);
            if(cameraMap == null ){
                cameraMap = new HashMap<String,Integer>();
                staticMap.put(cameraSn,cameraMap);
            }
            //获取该摄像头的统计
            getCameraStatistic(detections,cameraMap);
        }
        logger.debug("数据处理后的统计值:{}",JSON.toJSONString(staticMap));
        if(staticMap.isEmpty()){
            logger.info("数据处理后的统计值为空,结束本次统计");
            return null;
        }
        //开始计算每个摄像头的统计值
        List<StatisticEntity> entities = new ArrayList<StatisticEntity>();
        staticMap.forEach((key,value)->{
            StatisticEntity statisticEntity = new StatisticEntity();
            Map currentMap = new HashMap<String,Integer>();
            //人流量
            int pepoleCount = countData((HashMap<String,Integer>)value,CommonConfig.DETECT_PEOPLE_TYPE);
            int carCount = countData((HashMap<String,Integer>)value,CommonConfig.DETECT_CAR_TYPE);
            currentMap.put(String.valueOf(CommonConfig.DETECT_PEOPLE_TYPE),pepoleCount);
            currentMap.put(String.valueOf(CommonConfig.DETECT_CAR_TYPE),carCount);
            statisticEntity.setCameraSn((String)key);
            statisticEntity.setData(currentMap);
            statisticEntity.setStatisticsTime(starPreHourtTime);
            statisticEntity.setCreateTime(new Date());
            entities.add(statisticEntity);
        });
        return entities;
    }

    private void getCameraStatistic(ArrayList<JSONObject> detections,Map<String,Integer> cameraMap){
        for(JSONObject result:detections){
            String type = result.getString("type");
            Integer objectCount =  result.getInteger("objectCount");
            if(StringUtils.isEmpty(type) || objectCount == null){
                logger.info("对于类型:{},没有objectCount数据,跳过统计",type);
                continue;
            }
            Integer min = (Integer)cameraMap.get(MIN_PRE+type);
            Integer max = (Integer)cameraMap.get(MAX_PRE+type);
            if(min == null && max == null){
                cameraMap.put(MIN_PRE+type,objectCount);
                cameraMap.put(MAX_PRE+type,objectCount);
            }else if(min != null && max == null){
                if(objectCount < min){
                    cameraMap.put(MIN_PRE+type,objectCount);
                    cameraMap.put(MAX_PRE+type,min);
                }else{
                    cameraMap.put(MAX_PRE+type,objectCount);
                }
            }else if(min == null && max != null){
                if(objectCount > max){
                    cameraMap.put(MAX_PRE+type,objectCount);
                    cameraMap.put(MIN_PRE+type,max);
                }else {
                    cameraMap.put(MIN_PRE+type,objectCount);
                }
            }else if(min != null && max != null){
                if(objectCount < min){
                    cameraMap.put(MIN_PRE+type,objectCount);
                }
                if(objectCount > max){
                    cameraMap.put(MAX_PRE+type,objectCount);
                }
            }
        }

    }

    private int countData(Map<String,Integer> value,int type){
        Integer  max= (Integer)((HashMap)value).get(MAX_PRE+type);
        Integer min = (Integer)((HashMap)value).get(MIN_PRE+type);
        if(max != null && min != null){
            return max-min;
        }else if(max != null && min == null){
             return max;
        }else if(max == null && min != null){
             return min;
        }else{
            return 0;
        }

    }

    @Override
    public void statisticDetecteData(DetectionEvent currentDetectionEevent, String cameraSn)  {
        logger.debug("统计数据，cameraSn:{},currentDetectionEevent:{}",cameraSn, JSON.toJSONString(currentDetectionEevent));
       try{
           //查找前一条数据
           DetectionEvent preDetectionEvent = detectionDao.findOneDetectonByCameraSnDesc(cameraSn);
           logger.debug("查找前一条上报的数据:{}",JSON.toJSONString(preDetectionEvent));
           if(preDetectionEvent == null){
               //标注当前为第一条数据
//            detectionDao.updateHourfirstFlag(currentDetectionEevent.getId(),true);
               logger.debug("没有前一条数据，直接返回");
               return;
           }
           Date preTimestamp = preDetectionEvent.getTimeStamp();
           Date currentTimestamp = currentDetectionEevent.getTimeStamp();
           String preTimestampStr = Utils.getCurrentHourTime(preTimestamp);
           String currentTimestampStr = Utils.getCurrentHourTime(currentTimestamp);
           logger.debug("当前数据时间:{},前一条数据时间:{}",currentTimestampStr,preTimestampStr);
           //无需统计
           if(preTimestampStr.equals(currentTimestampStr)){
               logger.debug("当前是非跨小时事件，无需统计");
               return;
           }

           //获取前一小时的首个数据
           Date preHourStartTime = Utils.strToDate(preTimestampStr+"0000");
           Date preHourEndTime = Utils.strToDate(preTimestampStr+"5959");
           logger.debug("查找cameraSn:{}，在{}到{}时间段的第一条数据",cameraSn,preHourStartTime,preHourEndTime);
           DetectionEvent preHourDetectionEvent = detectionDao.findOneHourDetectonByCameraSn(preHourStartTime,preHourEndTime,cameraSn);
           if(preHourDetectionEvent == null){//依旧找不到(理论上不可能)
               preHourDetectionEvent = preDetectionEvent;
           }
           logger.debug("找到记录:{}",JSON.toJSONString(preHourDetectionEvent));

           //零点处理,2023022800
           if("00".equals(currentTimestampStr.substring(8,10))){
               //最后一条数据为前一天的最后一条数据
               currentDetectionEevent = preDetectionEvent;
               logger.debug("当前是零点的第一条数据，需要获取前一天最后一条数据:{}",JSON.toJSONString(preDetectionEvent));
           }

           //判断前一个小时首个记录和最后一个记录是否为同一条,仅可能会出现在0点统计中
           if(preHourDetectionEvent.getId().equals(currentDetectionEevent.getId())){
               logger.debug("前一个时间段的数据与当前的时间段数据一致(0点统计流程):{}",JSON.toJSONString(preDetectionEvent));

               ArrayList<JSONObject> currentResults = preHourDetectionEvent.getDetectionResult();
               Map currentMap = new HashMap<String,Integer>();
               for(JSONObject result:currentResults){
                   String type = result.getString("type");
                   int objectCount =  result.getInteger("objectCount");
                   currentMap.put(type,objectCount);
               }
               StatisticEntity StatisticEntity = new StatisticEntity();
               StatisticEntity.setCameraSn(cameraSn);
               StatisticEntity.setData(currentMap);
               StatisticEntity.setStatisticsTime(preHourStartTime);
               statisticsDao.addStatisticsData(StatisticEntity);
               return;
           }
           //当前的记录
           ArrayList<JSONObject> currentResults =  currentDetectionEevent.getDetectionResult();
           Map currentMap = new HashMap<String,Integer>();
           for(JSONObject result:currentResults){
               String type = result.getString("type");
               int objectCount =  result.getInteger("objectCount");
               currentMap.put(type,objectCount);
           }
           logger.debug("当前的统计记录:{}",JSON.toJSONString(currentMap));
           ArrayList<JSONObject> preResults =  preHourDetectionEvent.getDetectionResult();
           Map preMap = new HashMap<String,Integer>();
           for(JSONObject result:preResults){
               String type = result.getString("type");
               int objectCount =  result.getInteger("objectCount");
               preMap.put(type,objectCount);
           }
           logger.debug("前一个时间段的统计记录:{}",JSON.toJSONString(preMap));
           Map resultMap = new HashMap<String,Integer>();
           currentMap.forEach((key, value) ->{
               int preValue = (Integer)preMap.get(key);
               int count = (int)value - preValue;
               if(count<0){
                   logger.error("统计失败，数据减少，类型为:{},前一个时间数据:{},后一段时间数据:{},",key,preValue,value);
               }
               resultMap.put(key,count);
           });
           logger.debug("该时间段:{},的统计记录:{}",preTimestampStr,JSON.toJSONString(resultMap));

           StatisticEntity StatisticEntity = new StatisticEntity();
           StatisticEntity.setCameraSn(cameraSn);
           StatisticEntity.setData(resultMap);
           StatisticEntity.setStatisticsTime(preHourStartTime);
           statisticsDao.addStatisticsData(StatisticEntity);
       }catch (BusinessException e){
           logger.error("统计异常，cameraSn:{},currentDetectionEevent:{}",cameraSn, JSON.toJSONString(currentDetectionEevent),e);
       }catch (Exception e1){
//           throw  new BusinessException(e1, ErrorCodeEnum.SERVER_CODE_ERROR);
           logger.error("统计异常，cameraSn:{},currentDetectionEevent:{}",cameraSn, JSON.toJSONString(currentDetectionEevent),e1);
       }
    }

    @Override
    public StatisticRespVo getStatisticData(String cameraSn, String date) {
        logger.debug("获取摄像头统计数据，cameraSn:{},date:{}",cameraSn, date);
        if(StringUtils.isEmpty(cameraSn) && StringUtils.isEmpty(date)){
            logger.error("获取摄像头统计数据失败,入参为空");
            throw  new BusinessException(ErrorCodeEnum.ILLEGAL_ARGUMENT_INAVLID);
        }
        StatisticRespVo  statisticRespVo = null;
        try{
            Date startTime = null;
            Date endTime = null;
            Boolean isToday = false;
            int lastHour = 24;
            if(!StringUtils.isEmpty(date)){
                startTime = Utils.strToDate(date+"000000");
                endTime = Utils.strToDate(date+"235959");
                String currentDay = Utils.getCurrentDayTime(new Date());
                if(date.equals(currentDay)){
                    isToday = true;
                    lastHour = Integer.valueOf(Utils.getHourTime(new Date()))+1;
                }
            }
            //当前一个小时
            String currentHourTimeStr = Utils.getCurrentHourTime(new Date());
            String startCurrentHourTimeStr = currentHourTimeStr+"0000";
            String endCurrentHourTimeStr = currentHourTimeStr+"5959";

            //查询某个摄像头某天的统计数据
            if(!StringUtils.isEmpty(cameraSn) && startTime != null && endTime != null){
                //当天历史数据
                List<StatisticEntity> statisticEntities =  statisticsDao.getStatisticEntityByCameraSn(cameraSn,startTime,endTime);
                //当前一小时数据
                if(isToday){
                    List<StatisticEntity> currentEntities = statisticForHour(startCurrentHourTimeStr,endCurrentHourTimeStr,cameraSn);
                    if(statisticEntities != null && currentEntities != null){
                        statisticEntities.addAll(currentEntities);
                    }else if(statisticEntities == null && currentEntities != null){
                        statisticEntities = currentEntities;
                    }
                }

                statisticRespVo = widgetStatistic(statisticEntities);
                complementRespVo(statisticRespVo,lastHour);
            }else if(StringUtils.isEmpty(cameraSn) && startTime != null && endTime != null){//查询所有摄像头某天的数据
                List<StatisticEntity> statisticEntities =  statisticsDao.getStatisticEntityByDate(startTime,endTime);
                //当前一小时数据
                if(isToday){
                    List<StatisticEntity> currentEntities = statisticForHour(startCurrentHourTimeStr,endCurrentHourTimeStr,null);
                    if(statisticEntities != null && currentEntities != null){
                        statisticEntities.addAll(currentEntities);
                    }else if(statisticEntities == null && currentEntities != null){
                        statisticEntities = currentEntities;
                    }
                }
                statisticRespVo = widgetStatistic(statisticEntities);
                complementRespVo(statisticRespVo,lastHour);
            }else if(!StringUtils.isEmpty(cameraSn) && startTime == null && endTime == null){//查询某个摄像头实时数据
                DetectionEvent detectionEvent = detectionDao.findLastDetectonByCameraSn(cameraSn);
                statisticRespVo =  widgetStatistic(detectionEvent);
           }
        }catch (BusinessException e){
            throw e;
        }catch (Exception e1){
            throw  new BusinessException(e1, ErrorCodeEnum.SERVER_CODE_ERROR);
        }

        return statisticRespVo;
    }

    private void complementRespVo(StatisticRespVo statisticRespVo,int lastHour){
        logger.debug("补全摄像头统计数据，statisticRespVo:{}",JSON.toJSONString(statisticRespVo));
        if(statisticRespVo ==  null){
           return;
        }
        ArrayList<JSONObject> details = statisticRespVo.getDetail();
        if(details == null){
            logger.debug("补全摄像头统计数据，detail为null，无需补全");
            return;
        }
        for(int i = 0; i<lastHour;i++){
            if(details.size() == i){
                //补全
                JSONObject obj = getVoObj(i);
                details.add(i,obj);
            }
            JSONObject detailObj =  (JSONObject)details.get(i);

            String hour = detailObj.getString(CommonConfig.DETECT_HOUR);
            int hourInt = Integer.valueOf(hour);
            if(hourInt != i ){
                //补充
                JSONObject obj = getVoObj(i);
                details.add(i,obj);
            }
        }
    }
    private JSONObject getVoObj(int index){
        JSONObject obj = new JSONObject();
        String currentHour="0";
        if(index<10){
            currentHour = "0"+index;
        }else{
            currentHour = String.valueOf(index);
        }
        obj.put(CommonConfig.DETECT_PEOPLE_NUM,0);
        obj.put(CommonConfig.DETECT_CAR_NUM,0);
        obj.put(CommonConfig.DETECT_HOUR,currentHour);
        logger.debug("补全摄像头统计数据，obj:{}",JSON.toJSONString(obj));
        return obj;
    }

    private StatisticRespVo widgetStatistic(List<StatisticEntity> statisticEntities){
        logger.debug("封装统计数据，statisticEntities:{}",JSON.toJSONString(statisticEntities));
        ArrayList<JSONObject> detail = new ArrayList<JSONObject>();
        Map<String,Map> detailMap = new HashMap<String,Map>();
        for(StatisticEntity statisticEntity : statisticEntities ){
            Date  statisticTime = statisticEntity.getStatisticsTime();
            String statisticStr = Utils.getHourTime(statisticTime);
            int currentPeopleNum = (Integer) statisticEntity.getData().get(String.valueOf(CommonConfig.DETECT_PEOPLE_TYPE));
            int currentCarNum = (Integer)statisticEntity.getData().get(String.valueOf(CommonConfig.DETECT_CAR_TYPE));
            logger.debug("时间段:{},实时人员数量:{},实时车辆数量:{}",statisticStr,currentPeopleNum,currentCarNum);
            Map statisticData = detailMap.get(statisticStr);
            if(statisticData == null){
                statisticData = new HashMap();
                statisticData.put(CommonConfig.DETECT_PEOPLE_TYPE,0);
                statisticData.put(CommonConfig.DETECT_CAR_TYPE,0);
                detailMap.put(statisticStr,statisticData);
            }
            int peopleNum = (Integer)statisticData.get(CommonConfig.DETECT_PEOPLE_TYPE);
            int carNum = (Integer)statisticData.get(CommonConfig.DETECT_CAR_TYPE);
            peopleNum += currentPeopleNum;
            carNum += currentCarNum;
            statisticData.put(CommonConfig.DETECT_PEOPLE_TYPE,peopleNum);
            statisticData.put(CommonConfig.DETECT_CAR_TYPE,carNum);
        }
        logger.debug("排序前统计数据值，detailMap:{}",JSON.toJSONString(detailMap));
        //排序
        Map<String, Object> sortMap = new TreeMap<String, Object>(new Comparator<String>() {
            @Override
            public int compare(String o1,String o2) {
                return ((String)o1).compareTo((String) o2);
            }
        });
        sortMap.putAll(detailMap);
        logger.debug("排序后统计数据值，sortMap:{}",JSON.toJSONString(sortMap));

        //封装
        sortMap.forEach((key, value) ->{
            Map hourStatistisData = (Map)sortMap.get(key);
            int hourPeopleNum = (Integer) hourStatistisData.get(CommonConfig.DETECT_PEOPLE_TYPE);
            int hourCarNum = (Integer) hourStatistisData.get(CommonConfig.DETECT_CAR_TYPE);
            JSONObject detailObj = new JSONObject();
            detailObj.put(CommonConfig.DETECT_PEOPLE_NUM,hourPeopleNum);
            detailObj.put(CommonConfig.DETECT_CAR_NUM,hourCarNum);
            detailObj.put(CommonConfig.DETECT_HOUR,key);
            detail.add(detailObj);
        });

        StatisticRespVo statisticRespVo = new StatisticRespVo();
        statisticRespVo.setDetail(detail);
        logger.debug("最终的统计数据值，statisticRespVo:{}",JSON.toJSONString(statisticRespVo));
       return  statisticRespVo;
    }

    private StatisticRespVo widgetStatistic(DetectionEvent detectionEvent){
        logger.debug("封装实时数据，detectionEvent:{}",JSON.toJSONString(detectionEvent));
        StatisticRespVo statisticRespVo = new StatisticRespVo();
        if(detectionEvent == null){
           return statisticRespVo;
        }
        ArrayList<JSONObject> dectectList = detectionEvent.getDetectionResult();
        Map<String,Integer> cameraMap = new HashMap<String,Integer>();
        getCameraStatistic(dectectList,cameraMap);
        if(cameraMap != null && !cameraMap.isEmpty() ){
            int pepoleCount = countData(cameraMap,CommonConfig.DETECT_PEOPLE_TYPE);
            int carCount = countData(cameraMap,CommonConfig.DETECT_CAR_TYPE);
            statisticRespVo.setPeopleCount(pepoleCount);
            statisticRespVo.setCarCount(carCount);
        }
//        for(JSONObject detectObj : dectectList){
//            int detectType = Integer.valueOf((String)detectObj.get(CommonConfig.DETECT_TYPE));
//            int objectCount = (Integer) detectObj.get(CommonConfig.DETECT_OBJECTCOUNT);
//            if(detectType == CommonConfig.DETECT_PEOPLE_TYPE){
//                statisticRespVo.setPeopleCount(objectCount);
//            }else if (detectType == CommonConfig.DETECT_CAR_TYPE){
//                statisticRespVo.setCarCount(objectCount);
//            }
//        }
        JSONObject imageObj = detectionEvent.getImage();
        if(imageObj!=null){
            String imagePath = imageObj.getString(CommonConfig.FILE_PATH);
            statisticRespVo.setImageUrl(pathUrl+imagePath);
        }
        logger.debug("最终实时数据，statisticRespVo:{}",JSON.toJSONString(statisticRespVo));
        return statisticRespVo;

    }
}
