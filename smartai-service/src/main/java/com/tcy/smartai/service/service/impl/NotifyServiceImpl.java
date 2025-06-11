package com.tcy.smartai.service.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tcy.smartai.service.common.CommonConfig;
import com.tcy.smartai.service.common.OkHttpCli;
import com.tcy.smartai.service.dao.CamerasDao;
import com.tcy.smartai.service.dao.ConfigDao;
import com.tcy.smartai.service.entity.CamerasEntity;
import com.tcy.smartai.service.entity.ConfigEntity;
import com.tcy.smartai.service.entity.EventEntity;
import com.tcy.smartai.service.enums.ErrorCodeEnum;
import com.tcy.smartai.service.exception.BusinessException;
import com.tcy.smartai.service.service.ConfigService;
import com.tcy.smartai.service.service.NotifyService;
import com.tcy.smartai.service.vo.EventRespVo;
import com.tcy.smartai.service.vo.GraspPicRespVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Configuration
@Service
public class NotifyServiceImpl implements NotifyService {
    public static Logger logger = LoggerFactory.getLogger(NotifyServiceImpl.class);
    @Autowired
    CamerasDao camerasDao;


    @Autowired
    private OkHttpCli okHttpCli;

    @Autowired
    ConfigService configService;

    @Value("${path.url.image}")
    private String pathUrl;

    @Override
    @Async("threadPoolTaskExecutor")
    public void sendEvent(EventEntity eventEntity) {
        logger.debug("发送事件,eventEntity:{}", JSON.toJSONString(eventEntity));
        try{
            //获取事件通知地址
            ConfigEntity configEntity = configService.findConfig();
            if(configEntity == null || StringUtils.isEmpty(configEntity.getEventUrl())){
                logger.error("发送事件通知失败，没有配置事件地址,eventEntity:{}",JSON.toJSONString(eventEntity));
                return;
            }
            String url = configEntity.getEventUrl();
            EventRespVo eventRespBean = new EventRespVo();
            eventRespBean.setCameraSn(eventEntity.getCameraSn());
            eventRespBean.setEventUuid(eventEntity.getGraspCode());
            ArrayList<String> images =new ArrayList<String>();
            images.add(pathUrl+eventEntity.getEventPicture());
            eventRespBean.setImages(images);
            Date timeStamp = eventEntity.getTimeStamp();
            eventRespBean.setTimestamp(timeStamp.getTime());

            //获取摄像头的位置信息
//            String cameraSn = eventEntity.getCameraSn();
//            CamerasEntity camerasEntity = camerasDao.findCameraBySn(cameraSn);
//            if(camerasEntity != null){
//                logger.debug("获取摄像头地址:{}",camerasEntity.getAddress());
//                eventRespBean.setAddress(camerasEntity.getAddress());
//            }
            //获取事件名称TODO
//            eventRespBean.setEventName(CommonConfig.getEventName(eventEntity.getEventCode()));
            eventRespBean.setEventCode(eventEntity.getEventCode());
            eventRespBean.setRemark(eventEntity.getEventDesc());
            String requestStr = JSON.toJSONString(eventRespBean);
            logger.debug("开始发送事件,url:{},reques:{}", url,requestStr);
            String responseStr = okHttpCli.doPostJson(url,requestStr);
            logger.debug("发送事件成功,responseStr:{}", responseStr);
        }catch (Exception e){
            logger.error("发送事件失败:{}",JSON.toJSONString(eventEntity),e);
        }
    }

    @Override
    @Async("threadPoolTaskExecutor")
    public void sendGrasp(String graspCode, ArrayList<JSONObject> graspPictures) {
        logger.debug("发送抓拍事件,graspCode:{}，graspPictures:{}", graspCode, JSON.toJSONString(graspPictures));
        if (StringUtils.isEmpty(graspCode)) {
            logger.error("发送抓拍事件失败,缺少graspCode参数");
            throw new BusinessException(ErrorCodeEnum.EVENT_PARAM_GRASP_NULL);
        }
        try {
            //获取事件通知地址
            ConfigEntity configEntity = configService.findConfig();
            String url = configEntity.getEventUrl();
            if (StringUtils.isEmpty(url)) {
                logger.error("发送事件更新失败，没有配置事件地址");
                return;
            }
            GraspPicRespVo graspPicRespVo = new GraspPicRespVo();
            graspPicRespVo.setOp("add");
            graspPicRespVo.setPath("/"+graspCode+"/images");
            List images = new ArrayList();
            if (graspPictures != null && graspPictures.size() > 0) {
                for (JSONObject graspPicture : graspPictures) {
                    String imageUrl = (String) graspPicture.get(CommonConfig.UPLOAD_PICTURE_PATH);
                    images.add(pathUrl+imageUrl);
                }
            }
            graspPicRespVo.setValue(images);
            String requestStr = JSON.toJSONString(graspPicRespVo);
            logger.debug("开始发送抓拍事件,url:{},reques:{}", url, requestStr);
            String responseStr = okHttpCli.doPatchJson(url, requestStr);
            logger.debug("发送抓拍事件成功,response:{}", responseStr);
        } catch (Exception e) {
            logger.error("发送抓拍事件失败", e);
        }
    }
}
