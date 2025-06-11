package com.tcy.smartai.service.dao;

import com.mongodb.client.result.UpdateResult;
import com.tcy.smartai.service.entity.NetEqEntity;

import java.util.List;

public interface NetEqDao {

    void addNetEq(NetEqEntity netEqEntity);

    void updateNetEq(NetEqEntity netEqEntity);

    NetEqEntity findNetEqById(String NetEqId);

    void updateHeartbeat(String NetEqId);

    List<NetEqEntity> findAllNetEq();
}
