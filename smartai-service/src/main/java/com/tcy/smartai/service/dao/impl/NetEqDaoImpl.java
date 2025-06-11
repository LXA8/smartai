package com.tcy.smartai.service.dao.impl;

import com.mongodb.client.result.UpdateResult;
import com.tcy.smartai.service.dao.NetEqDao;
import com.tcy.smartai.service.entity.NetEqEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class NetEqDaoImpl implements NetEqDao {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Override
    public void addNetEq(NetEqEntity netEqEntity) {
        netEqEntity.setCreateTime(new Date());
        netEqEntity.setHeartbeatTime(new Date());
        mongoTemplate.save(netEqEntity);
    }

    @Override
    public void updateNetEq(NetEqEntity netEqEntity) {
        Query query = Query.query(Criteria.where("neteq_id").is(netEqEntity.getNetEqID()));
        Update update = new Update();
        update.set("desc",netEqEntity.getDesc());
        update.set("create_time",netEqEntity.getCreateTime());
        update.set("update_time",new Date());
        update.set("camera_rec_version",netEqEntity.getCameraRecVersion());
        update.set("camera_total",netEqEntity.getCameraTotal());
        mongoTemplate.updateFirst(query,update,"neteq");
    }

    @Override
    public NetEqEntity findNetEqById(String NetEqId) {
        Query query = Query.query(Criteria.where("neteq_id").is(NetEqId));
        return mongoTemplate.findOne(query, NetEqEntity.class);
    }

    @Override
    public void updateHeartbeat(String NetEqId) {
        Query query = Query.query(Criteria.where("neteq_id").is(NetEqId));
        Update update = new Update();
        update.set("heartbeat_time",new Date());
        mongoTemplate.updateFirst(query,update,"neteq");
    }

    @Override
    public List<NetEqEntity> findAllNetEq() {
        return mongoTemplate.findAll(NetEqEntity.class,"neteq");
    }
}
