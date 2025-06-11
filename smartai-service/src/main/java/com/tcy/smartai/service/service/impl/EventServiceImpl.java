package com.tcy.smartai.service.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tcy.smartai.service.common.CommonConfig;
import com.tcy.smartai.service.service.NotifyService;
import com.tcy.smartai.service.vo.EventRespVo;
import com.tcy.smartai.service.common.OkHttpCli;
import com.tcy.smartai.service.dao.CamerasDao;
import com.tcy.smartai.service.dao.ConfigDao;
import com.tcy.smartai.service.dao.EventDao;
import com.tcy.smartai.service.entity.CamerasEntity;
import com.tcy.smartai.service.entity.ConfigEntity;
import com.tcy.smartai.service.entity.EventEntity;
import com.tcy.smartai.service.enums.ErrorCodeEnum;
import com.tcy.smartai.service.exception.BusinessException;
import com.tcy.smartai.service.service.EventService;
import com.tcy.smartai.service.vo.EventVo;
import com.tcy.smartai.service.vo.GraspPicRespVo;
import com.tcy.smartai.service.vo.GraspPicVo;
import org.apache.tomcat.jni.Thread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    public static Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);

    @Autowired
    EventDao eventDao;

    @Autowired
    CamerasDao camerasDao;

    @Autowired
    ConfigDao configDao;


    @Autowired
    private NotifyService notifyService;


    @Override
    public void addEvent(EventVo eventVo) throws BusinessException {
         logger.debug("添加事件,eventVo:{}", JSON.toJSONString(eventVo));
        if(StringUtils.isEmpty(eventVo)){
            logger.error("添加事件失败,入参为空");
            throw  new BusinessException(ErrorCodeEnum.ILLEGAL_ARGUMENT_INAVLID);
        }

        try{
            String cameraSn = eventVo.getCameraSn();
            String eventPicture = eventVo.getEventPicture();
            String eventCode = eventVo.getEventCode();
            String eventDesc= eventVo.getEventDesc();
            String needAlarm = eventVo.getNeedAlarm();
            String timeStamp = eventVo.getTimeStamp();
            String graspCode = eventVo.getGraspCode();
            if(StringUtils.isEmpty(cameraSn) ){
                logger.error("添加事件失败,cameraSn为空");
                throw  new BusinessException(ErrorCodeEnum.EVENT_SN_NULL);
            }
            EventEntity oldEventEntity = eventDao.findEventByGraspCode(cameraSn,graspCode);
            if(oldEventEntity != null){
                logger.error("添加事件失败,该事件已存在，oldEventEntity:{}",oldEventEntity);
                throw  new BusinessException(ErrorCodeEnum.EVENT_HAS_EXIST);
            }

            EventEntity eventEntity = new EventEntity();
            if(StringUtils.isEmpty(timeStamp)){
                eventEntity.setTimeStamp(new Date());
            }else{
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                Date timeStmpDate = sdf.parse(timeStamp);
                eventEntity.setTimeStamp(timeStmpDate);
            }
            eventEntity.setCameraSn(cameraSn);
            eventEntity.setEventCode(eventCode);
            eventEntity.setEventPicture(eventPicture);
            eventEntity.setEventDesc(eventDesc);
            eventEntity.setNeedAlarm(needAlarm);
            eventEntity.setGraspCode(graspCode);
            eventDao.addEvent(eventEntity);
            logger.debug("添加事件成功:{}",JSON.toJSONString(eventEntity));
            //异步发送事件
            if("1".equals(needAlarm) && eventCode.startsWith("E")){
                logger.debug("开始发送事件");
                notifyService.sendEvent(eventEntity);
            }
        }catch (BusinessException e1){
           throw e1;
        } catch(Exception e){
            throw  new BusinessException(e,ErrorCodeEnum.SERVER_CODE_ERROR);
        }
    }



    @Override
    public void addGraspPic(GraspPicVo graspPicVo) throws BusinessException {
        logger.debug("添加抓拍结果上报,graspPicVo:{}", JSON.toJSONString(graspPicVo));
        if(StringUtils.isEmpty(graspPicVo)){
            logger.error("添加抓拍结果上报失败,入参为空");
            throw  new BusinessException(ErrorCodeEnum.ILLEGAL_ARGUMENT_INAVLID);
        }
        String cameraSn = graspPicVo.getCameraSn();
        String graspCode = graspPicVo.getGraspCode();
        ArrayList<JSONObject> graspPictures = graspPicVo.getGraspPictures();
        if(StringUtils.isEmpty(cameraSn) || StringUtils.isEmpty(graspCode)||graspPictures == null ||graspPictures.size()==0){
            logger.error("添加抓拍结果上报失败,参数不正确");
            throw  new BusinessException(ErrorCodeEnum.EVENT_PARAM_NOT_OK);
        }
        try{
            eventDao.updateGraspPictures(cameraSn,graspCode,graspPictures);
            notifyService.sendGrasp(graspCode,graspPictures);
        }catch (Exception e){
            throw  new BusinessException(e,ErrorCodeEnum.SERVER_CODE_ERROR);
        }

    }

}
