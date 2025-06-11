package com.tcy.smartai.service.service;

import com.tcy.smartai.service.entity.CamerasEntity;
import com.tcy.smartai.service.exception.BusinessException;
import com.tcy.smartai.service.vo.CameraRespVo;
import com.tcy.smartai.service.vo.CameraVo;

import java.util.List;

public interface CameraService {
    void addCameras(CameraVo cameraVo) throws BusinessException;

    void updateCameras(String cameraSn,CameraVo cameraVo) throws BusinessException;

    void deleteCameras(String cameraSn) throws BusinessException;

    List<CameraRespVo> getCamerasByNetEq(String netEqID)throws BusinessException;
}
