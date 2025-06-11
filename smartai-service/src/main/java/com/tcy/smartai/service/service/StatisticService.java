package com.tcy.smartai.service.service;

import com.tcy.smartai.service.entity.DetectionEvent;
import com.tcy.smartai.service.entity.StatisticEntity;
import com.tcy.smartai.service.exception.BusinessException;
import com.tcy.smartai.service.vo.StatisticRespVo;

import java.text.ParseException;
import java.util.List;

public interface StatisticService {

    void statisticDetecteData(DetectionEvent currentDetectionEevent, String cameraSn) throws BusinessException;

    StatisticRespVo getStatisticData(String cameraSn, String date);

    void scheduleDetecteData();

}
