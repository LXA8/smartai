package com.tcy.smartai.service.dao;

import com.tcy.smartai.service.entity.StatisticEntity;

import java.util.Date;
import java.util.List;

public interface StatisticsDao {

    public void addStatisticsData(StatisticEntity statisticEntity);
    List<StatisticEntity> getStatisticEntityByCameraSn(String cameraSn, Date startTime, Date endTime);
    List<StatisticEntity> getStatisticEntityByDate(Date startTime, Date endTime);
    void batchInsert(List<StatisticEntity> entities);
}
