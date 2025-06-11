package com.tcy.smartai.service.service.impl;

import com.alibaba.fastjson.JSON;
import com.tcy.smartai.service.dao.CamerasDao;
import com.tcy.smartai.service.entity.CamerasEntity;
import com.tcy.smartai.service.entity.NetEqEntity;
import com.tcy.smartai.service.enums.ErrorCodeEnum;
import com.tcy.smartai.service.exception.BusinessException;
import com.tcy.smartai.service.service.CameraService;
import com.tcy.smartai.service.service.NetEqService;
import com.tcy.smartai.service.vo.CameraRespVo;
import com.tcy.smartai.service.vo.CameraVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class CameraServiceImpl implements CameraService  {

    public static Logger logger = LoggerFactory.getLogger(CameraServiceImpl.class);

    @Autowired
    CamerasDao camerasDao;

    @Autowired
    NetEqService netEqService;


    @Override
    public void addCameras(CameraVo cameraVo) throws BusinessException {
        logger.debug("添加摄像头,cameraVo:{}", JSON.toJSONString(cameraVo));
        if(StringUtils.isEmpty(cameraVo)){
            logger.error("添加摄像头失败,入参为空");
            throw  new BusinessException(ErrorCodeEnum.ILLEGAL_ARGUMENT_INAVLID);
        }

        try{
            String cameraSn = cameraVo.getCameraSn();
            CamerasEntity oldCamerasEntity = camerasDao.findCameraBySn(cameraSn);
            if(oldCamerasEntity != null){
                throw  new BusinessException(ErrorCodeEnum.CAMERAS_SN_EXIST);
            }
            NetEqEntity netEqEntity = netEqService.dynamicAllocationCamera();
            if(netEqEntity == null){
                throw  new BusinessException(ErrorCodeEnum.CAMERAS_ALLOCATION_FAIL);
            }
            CamerasEntity entity = new CamerasEntity();
            BeanUtils.copyProperties(cameraVo,entity);
            entity.setNetEqID(netEqEntity.getNetEqID());
            camerasDao.addCamera(entity);
            logger.debug("添加摄像头成功,CamerasEntity:{}", JSON.toJSONString(entity));
        }catch (BusinessException e1){
            throw e1;
        } catch(Exception e){
            throw  new BusinessException(e,ErrorCodeEnum.SERVER_CODE_ERROR);
        }
    }

    @Override
    public void updateCameras(String cameraSn,CameraVo cameraVo) throws BusinessException {
        logger.debug("更新摄像头,cameraSn:{},cameraVo:{}", cameraSn,JSON.toJSONString(cameraVo));
        if(StringUtils.isEmpty(cameraVo)||StringUtils.isEmpty(cameraSn)){
            logger.error("更新摄像头失败,入参为空");
            throw  new BusinessException(ErrorCodeEnum.ILLEGAL_ARGUMENT_INAVLID);
        }
        try{
            CamerasEntity camerasEntity = camerasDao.findCameraBySn(cameraSn);
            if(camerasEntity == null){
                logger.error("更新摄像头失败,没有找到该摄像头:{}",cameraSn);
                throw  new BusinessException(ErrorCodeEnum.CAMERAS_SN_NOT_EXIST);
            }
            CamerasEntity entity = new CamerasEntity();
            BeanUtils.copyProperties(cameraVo,entity);
            camerasDao.updateCamera(cameraSn,entity);
            String netEqId = camerasEntity.getNetEqID();
            netEqService.changeVersion(netEqId);
            logger.debug("更新摄像头成功,CamerasEntity:{}", JSON.toJSONString(entity));
        }catch (BusinessException e1){
            throw e1;
        }catch (Exception e){
            throw  new BusinessException(e,ErrorCodeEnum.SERVER_CODE_ERROR);
        }
    }

    @Override
    public void deleteCameras(String cameraSn) throws BusinessException {
        logger.debug("删除摄像头,cameraSn:{}",cameraSn);
        if(StringUtils.isEmpty(cameraSn)){
            logger.error("删除摄像头失败,入参为空");
            throw  new BusinessException(ErrorCodeEnum.ILLEGAL_ARGUMENT_INAVLID);
        }
        try{
            //查找该摄像头
            CamerasEntity camerasEntity = camerasDao.findCameraBySn(cameraSn);
            if(camerasEntity == null){
                logger.error("删除摄像头失败，该摄像头不存在:{}",cameraSn);
                throw  new BusinessException(ErrorCodeEnum.CAMERAS_SN_NOT_EXIST);
            }
            camerasDao.deleteCamera(cameraSn);
            String netEqId = camerasEntity.getNetEqID();
            netEqService.dynamicReduceCamera(netEqId);
            logger.debug("删除摄像头成功:{}",cameraSn);
        }catch (BusinessException e1){
            throw e1;
        }catch (Exception e){
            throw  new BusinessException(e,ErrorCodeEnum.SERVER_CODE_ERROR);
        }
    }

    public List<CameraRespVo> getCamerasByNetEq(String netEqID)throws BusinessException{
        logger.debug("根据网元id获取摄像头列表信息,netEqID:{}",netEqID);
        if(StringUtils.isEmpty(netEqID)){
            logger.error("获取摄像头列表信息失败,netEqID为空");
            throw  new BusinessException(ErrorCodeEnum.NETEQ_ID_NULL);
        }
        List<CameraRespVo> cameraRespVos = new ArrayList<>();
        try{
            List<CamerasEntity> camerasEntities = camerasDao.findCameraByNetEqId(netEqID);
            for(CamerasEntity camerasEntity: camerasEntities){
                CameraRespVo cameraRespVo = new CameraRespVo();
                BeanUtils.copyProperties(camerasEntity,cameraRespVo);
                cameraRespVos.add(cameraRespVo);
            }
            logger.debug("获取摄像头列表信息成功,camerasEntities:{}",camerasEntities);
        }catch (BusinessException e1){
            throw e1;
        }catch (Exception e){
            throw  new BusinessException(e,ErrorCodeEnum.SERVER_CODE_ERROR);
        }
        return cameraRespVos;
    }
}
