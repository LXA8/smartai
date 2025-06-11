package com.tcy.smartai.service.dao.impl;

import com.tcy.smartai.service.dao.EventDao;
import com.tcy.smartai.service.entity.EventEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class EventDaoImpl implements EventDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void addEvent(EventEntity eventEntity) {
        eventEntity.setCreateTime(new Date());
        mongoTemplate.save(eventEntity);
    }

    @Override
    public EventEntity findEventByGraspCode(String cameraSn, String graspCode) {
        Query query = Query.query(Criteria.where("camera_sn").is(cameraSn).and("grasp_code").is(graspCode));
        return mongoTemplate.findOne(query,EventEntity.class);
    }

    @Override
    public void updateEvent(EventEntity eventEntity) {
        Query query = Query.query(Criteria.where("camera_sn").is(eventEntity.getCameraSn()).and("grasp_code").is(eventEntity.getGraspCode()));
        Update update = new Update();
        update.set("grasp_pictures",eventEntity.getGraspPictures());
        update.set("update_time",new Date());
        mongoTemplate.updateFirst(query,update,"event");
    }

    @Override
    public void updateGraspPictures(String cameraSn, String graspCode, List graspPictures) {
        Query query = Query.query(Criteria.where("camera_sn").is(cameraSn).and("grasp_code").is(graspCode));
        Update update = new Update();
        update.push("grasp_pictures").each(graspPictures);
        update.set("update_time",new Date());
        mongoTemplate.updateFirst(query,update,"event");
    }


}
