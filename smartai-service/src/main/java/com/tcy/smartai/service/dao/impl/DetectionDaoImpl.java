package com.tcy.smartai.service.dao.impl;

import com.tcy.smartai.service.dao.DetectionDao;
import com.tcy.smartai.service.entity.CamerasEntity;
import com.tcy.smartai.service.entity.DetectionEvent;
import com.tcy.smartai.service.entity.StatisticEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class DetectionDaoImpl implements DetectionDao {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Override
    public DetectionEvent addDetection(DetectionEvent detectionEvent) {
        detectionEvent.setCreateTime(new Date());
        return  mongoTemplate.save(detectionEvent);
    }

    @Override
    public DetectionEvent findOneDetectonByCameraSnDesc(String cameraSn){
        Query query = new Query(Criteria.where("camera_sn").is(cameraSn));
        query.with(Sort.by(new Sort.Order(Sort.Direction.DESC, "time_stamp")));
        query.limit(1);
        return mongoTemplate.findOne(query, DetectionEvent.class);
    }




    @Override
    public DetectionEvent findOneHourDetectonByCameraSn(Date startTime, Date endTime, String cameraSn) {
        Query query = new Query(Criteria.where("camera_sn").is(cameraSn).andOperator(Criteria.where("time_stamp").lt(endTime),Criteria.where("time_stamp").gte(startTime)));
        query.with(Sort.by(new Sort.Order(Sort.Direction.ASC, "time_stamp")));
        query.limit(1);
        return mongoTemplate.findOne(query, DetectionEvent.class);
    }

    @Override
    public DetectionEvent findLastDetectonByCameraSn(String cameraSn) {
        Query query = new Query(Criteria.where("camera_sn").is(cameraSn));
        query.with(Sort.by(new Sort.Order(Sort.Direction.DESC, "time_stamp")));
        query.limit(1);
        return mongoTemplate.findOne(query, DetectionEvent.class);
    }

    @Override
    public List<DetectionEvent> findOneHourAllDetecton(Date startTime, Date endTime) {
        Query query = new Query(Criteria.where("time_stamp").lte(endTime).andOperator(Criteria.where("time_stamp").gte(startTime)));
        query.with(Sort.by(new Sort.Order(Sort.Direction.ASC, "time_stamp")));
         List<DetectionEvent> entities =  mongoTemplate.find(query, DetectionEvent.class,"detection");
        return entities;
    }

    @Override
    public List<DetectionEvent> findOneHourAllDetectonByCameraSn(Date startTime, Date endTime, String cameraSn) {
        Query query = new Query(Criteria.where("camera_sn").is(cameraSn).andOperator(Criteria.where("time_stamp").lt(endTime),Criteria.where("time_stamp").gte(startTime)));
        query.with(Sort.by(new Sort.Order(Sort.Direction.ASC, "time_stamp")));
        return mongoTemplate.find(query, DetectionEvent.class);
    }
}
