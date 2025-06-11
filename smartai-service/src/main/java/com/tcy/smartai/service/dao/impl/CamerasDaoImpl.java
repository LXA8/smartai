package com.tcy.smartai.service.dao.impl;

import com.tcy.smartai.service.dao.CamerasDao;
import com.tcy.smartai.service.entity.CamerasEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Repository
public class CamerasDaoImpl implements CamerasDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public CamerasEntity findCameraBySn(String cameraSn) {
        Query query = new Query(Criteria.where("camera_sn").is(cameraSn).and("is_delete").is(false));
        return mongoTemplate.findOne(query, CamerasEntity.class);
    }

    @Override
    public List<CamerasEntity> findCameraByNetEqId(String netEqId) {
        Query query = new Query(Criteria.where("neteq_id").is(netEqId).and("is_delete").is(false));
        return mongoTemplate.find(query, CamerasEntity.class);
    }

    @Override
    public void addCamera(CamerasEntity camerasEntity) {
        camerasEntity.setCreateTime(new Date());
        camerasEntity.setIsDelete(false);
        mongoTemplate.save(camerasEntity);
    }

    @Override
    public void updateCamera(String cameraSn,CamerasEntity camerasEntity) {
        Query query = Query.query(Criteria.where("camera_sn").is(cameraSn).and("is_delete").is(false));
        Update update = new Update();
        update.set("camera_sn",camerasEntity.getCameraSn());
        update.set("address",camerasEntity.getAddress());
        update.set("lngLat",camerasEntity.getLngLat());
        update.set("stream_url",camerasEntity.getStreamUrl());
        update.set("algorithms",camerasEntity.getAlgorithms());
        update.set("update_time",new Date());
        mongoTemplate.updateFirst(query,update,"cameras");

    }

    @Override
    public void deleteCamera(String cameraSn) {
        Query query = Query.query(Criteria.where("camera_sn").is(cameraSn).and("is_delete").is(false));
        Update update = new Update();
        update.set("is_delete",true);
        mongoTemplate.updateFirst(query,update,"cameras");

    }
}
