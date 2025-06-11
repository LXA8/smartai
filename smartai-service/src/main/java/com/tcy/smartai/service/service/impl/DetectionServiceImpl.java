package com.tcy.smartai.service.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tcy.smartai.service.common.Utils;
import com.tcy.smartai.service.dao.DetectionDao;
import com.tcy.smartai.service.entity.DetectionEvent;
import com.tcy.smartai.service.entity.StatisticEntity;
import com.tcy.smartai.service.enums.ErrorCodeEnum;
import com.tcy.smartai.service.exception.BusinessException;
import com.tcy.smartai.service.service.DetectionServcie;
import com.tcy.smartai.service.service.StatisticService;
import com.tcy.smartai.service.vo.DetectionVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.rmi.CORBA.Util;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class DetectionServiceImpl implements DetectionServcie {

    public static Logger logger = LoggerFactory.getLogger(DetectionServiceImpl.class);

    private  final String TIME_STAMP = "timeStamp";

    @Autowired
    DetectionDao detectionDao;


    @Override
    public void addDetection(DetectionVo detectionVo) throws BusinessException {
        logger.debug("检测上报,detectionVo:{}", JSON.toJSONString(detectionVo));
        if(StringUtils.isEmpty(detectionVo)){
            logger.error("检测上报失败,入参为空");
            throw  new BusinessException(ErrorCodeEnum.ILLEGAL_ARGUMENT_INAVLID);
        }
        try{
            JSONObject image = detectionVo.getImage();
            String cameraSn = detectionVo.getCameraSn();
            if(StringUtils.isEmpty(image)||StringUtils.isEmpty(cameraSn)){
                throw  new BusinessException(ErrorCodeEnum.DETECTION_IMAGE_CAMERA_NULL);
            }
            String timeStampStr = (String)image.get(TIME_STAMP);
            Date timeStmpDate = new Date();
            if(!StringUtils.isEmpty(timeStampStr)){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                timeStmpDate = sdf.parse(timeStampStr.substring(0,14));
            }
            DetectionEvent detectionEvent = new DetectionEvent();
            BeanUtils.copyProperties(detectionVo,detectionEvent);
            detectionEvent.setTimeStamp(timeStmpDate);
//            statisticService.statisticDetecteData(detectionEvent,cameraSn);
            detectionDao.addDetection(detectionEvent);
            logger.debug("检测上报成功:{}",JSON.toJSONString(detectionEvent));

        }catch (BusinessException e){
            throw e;
        }catch (Exception e1){
            throw  new BusinessException(e1,ErrorCodeEnum.SERVER_CODE_ERROR);
        }
    }


}
