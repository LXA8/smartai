package com.tcy.smartai.service.dao;

import com.tcy.smartai.service.entity.DetectionEvent;
import com.tcy.smartai.service.entity.StatisticEntity;

import java.util.Date;
import java.util.List;

public interface DetectionDao {
    DetectionEvent  addDetection(DetectionEvent detectionEvent);

    DetectionEvent findOneDetectonByCameraSnDesc(String cameraSn);

    DetectionEvent findOneHourDetectonByCameraSn(Date startTime, Date endTime,String cameraSn);

    List<DetectionEvent> findOneHourAllDetecton(Date startTime, Date endTime);

    List<DetectionEvent> findOneHourAllDetectonByCameraSn(Date startTime, Date endTime,String cameraSn);

    DetectionEvent findLastDetectonByCameraSn(String cameraSn);

}
