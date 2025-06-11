package com.tcy.smartai.service.dao.impl;

import com.tcy.smartai.service.dao.ConfigDao;
import com.tcy.smartai.service.entity.ConfigEntity;
import com.tcy.smartai.service.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class ConfigDaoImpl implements ConfigDao {

    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public void addConfig(ConfigEntity configEntity) {
        configEntity.setCreateTime(new Date());
        mongoTemplate.save(configEntity);
    }

    @Override
    public ConfigEntity findConfig(){
        return mongoTemplate.findOne(new Query(),ConfigEntity.class);
    }

    @Override
    public void updateConfig(ConfigEntity configEntity) {
        Query query = Query.query(Criteria.where("_id").is(configEntity.getId()));
        Update update = new Update();
        update.set("event_url",configEntity.getEventUrl());
        update.set("async_resp_url",configEntity.getAsyncRespUrl());
        update.set("update_time",new Date());
        mongoTemplate.updateFirst(query,update,"config");

    }
}
