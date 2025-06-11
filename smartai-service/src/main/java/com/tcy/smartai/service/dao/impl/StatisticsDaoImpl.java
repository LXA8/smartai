package com.tcy.smartai.service.dao.impl;

import com.tcy.smartai.service.dao.StatisticsDao;
import com.tcy.smartai.service.entity.StatisticEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class StatisticsDaoImpl implements StatisticsDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void addStatisticsData(StatisticEntity statisticEntity) {
        statisticEntity.setCreateTime(new Date());
        mongoTemplate.save(statisticEntity);
    }
    public List<StatisticEntity> getStatisticEntityByCameraSn(String cameraSn, Date startTime, Date endTime) {
        Query query = new Query(Criteria.where("camera_sn").is(cameraSn).andOperator(Criteria.where("statistics_time").lte(endTime),Criteria.where("statistics_time").gte(startTime)));
        query.with(Sort.by(new Sort.Order(Sort.Direction.ASC, "statistics_time")));
        List<StatisticEntity> entities = mongoTemplate.find(query,StatisticEntity.class,"statistics");
        return entities;
    }

    @Override
    public List<StatisticEntity> getStatisticEntityByDate(Date startTime, Date endTime) {
        Query query = new Query(Criteria.where("statistics_time").lte(endTime).andOperator(Criteria.where("statistics_time").gte(startTime)));
        query.with(Sort.by(new Sort.Order(Sort.Direction.ASC, "statistics_time")));
        List<StatisticEntity> entities =  mongoTemplate.find(query,StatisticEntity.class,"statistics");
        return entities;
    }

    @Override
    public void batchInsert(List<StatisticEntity> entities) {
        mongoTemplate.insert(entities,StatisticEntity.class);
    }
}
