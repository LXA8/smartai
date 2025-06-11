package com.tcy.smartai.service.dao;

import com.tcy.smartai.service.entity.CamerasEntity;

import java.util.List;

public interface CamerasDao {

    void addCamera(CamerasEntity camerasEntity);

    void updateCamera(String cameraSn,CamerasEntity camerasEntity);

    void deleteCamera(String cameraSn);

    CamerasEntity findCameraBySn(String cameraSn);

    List<CamerasEntity> findCameraByNetEqId(String netEqId);

}
