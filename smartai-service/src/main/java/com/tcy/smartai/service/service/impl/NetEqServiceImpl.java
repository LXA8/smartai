package com.tcy.smartai.service.service.impl;

import com.alibaba.fastjson.JSON;
import com.tcy.smartai.service.dao.NetEqDao;
import com.tcy.smartai.service.entity.NetEqEntity;
import com.tcy.smartai.service.enums.ErrorCodeEnum;
import com.tcy.smartai.service.exception.BusinessException;
import com.tcy.smartai.service.service.NetEqService;
import com.tcy.smartai.service.vo.NetEqVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;



@Service
public class NetEqServiceImpl implements NetEqService {

    public static Logger logger = LoggerFactory.getLogger(NetEqServiceImpl.class);
    @Autowired
    NetEqDao netEqDao;

    private final  long HEARTBEATDIFF = 5*60*1000;
    @Override
    public void registerNetEq(NetEqVo netEqVo) throws BusinessException {
        logger.debug("注册网元服务器,netEqVo:{}", JSON.toJSONString(netEqVo));
        if(StringUtils.isEmpty(netEqVo)){
            logger.error("注册网元服务器失败,入参为空");
            throw  new BusinessException(ErrorCodeEnum.ILLEGAL_ARGUMENT_INAVLID);
        }
        try{
            String netEqId = netEqVo.getNetEqID();
            String desc = netEqVo.getDesc();
            if(StringUtils.isEmpty(netEqId)){
                logger.error("注册网元服务器失败,网元Id为空");
                throw  new BusinessException(ErrorCodeEnum.NETEQ_ID_NULL);
            }

            NetEqEntity netEqEntity =  netEqDao.findNetEqById(netEqId);
            //重新注册，更新网元
            if(netEqEntity != null){
                netEqEntity.setCreateTime(new Date());
                netEqEntity.setCameraRecVersion(0);
                netEqEntity.setDesc(desc);
                netEqDao.updateNetEq(netEqEntity);
                logger.debug("更新网元成功:{}",JSON.toJSONString(netEqVo));
                return;
            }

            //新增
            NetEqEntity newNetEq =new NetEqEntity();
            newNetEq.setNetEqID(netEqId);
            newNetEq.setDesc(desc);
            newNetEq.setCameraRecVersion(0);
            newNetEq.setCameraTotal(0);
            logger.debug("新增网元成功:{}",JSON.toJSONString(netEqVo));
            netEqDao.addNetEq(newNetEq);

        }catch (BusinessException e1){
            throw e1;
        } catch(Exception e){
            throw  new BusinessException(e,ErrorCodeEnum.SERVER_CODE_ERROR);
        }

    }

    @Override
    public int heartbeatNetEq(NetEqVo netEqVo) throws BusinessException {
        logger.debug("网元服务器心跳,netEqVo:{}", JSON.toJSONString(netEqVo));
        if(StringUtils.isEmpty(netEqVo)){
            logger.error("网元服务器心跳失败,入参为空");
            throw  new BusinessException(ErrorCodeEnum.ILLEGAL_ARGUMENT_INAVLID);
        }
        try{
            String netEqId = netEqVo.getNetEqID();
            netEqDao.updateHeartbeat(netEqId);
            NetEqEntity netEqEntity = netEqDao.findNetEqById(netEqId);
            int recVersion = netEqEntity.getCameraRecVersion();
            return recVersion;
        }catch (BusinessException e1){
            throw e1;
        } catch(Exception e){
            throw  new BusinessException(e,ErrorCodeEnum.SERVER_CODE_ERROR);
        }
    }

    private List<NetEqEntity> findAllValidNetEq() {
        logger.debug("获取当前所有有效的网元服务器信息");
        List<NetEqEntity> netEqEntities = netEqDao.findAllNetEq();
        List<NetEqEntity> validEqEntities = new ArrayList<NetEqEntity>();
        if(netEqEntities != null && netEqEntities.size()>0){
            Date currentTime = new Date();
            for(NetEqEntity netEqEntity :netEqEntities){
                Date heartbeatTime = netEqEntity.getHeartbeatTime();
                //计算当前的时间与心跳的时间差
                Long diffTime = currentTime.getTime()-heartbeatTime.getTime();
                if(diffTime < HEARTBEATDIFF){
                    validEqEntities.add(netEqEntity);
                }
            }
        }
        logger.debug("当前所有有效的网元服务器信息:",JSON.toJSONString(validEqEntities));
        return validEqEntities;
    }

    @Override
    public NetEqEntity dynamicAllocationCamera() throws BusinessException{
        logger.debug("动态为摄像头分配网元服务器");
        List<NetEqEntity> netEqEntities =  findAllValidNetEq();
        if(netEqEntities == null || netEqEntities.size() == 0){
            throw  new BusinessException(ErrorCodeEnum.NETEQ_AVALID_NULL);
        }
        NetEqEntity allocationNetEq = null;
        try{
            int initTotal = 0;
            for (int i = 0;i<netEqEntities.size();i++){
                NetEqEntity netEqEntity = netEqEntities.get(i);
                if(i == 0){
                    allocationNetEq = netEqEntity;
                    initTotal = netEqEntity.getCameraTotal();
                    continue;
                }
                int total = netEqEntity.getCameraTotal();
                if(total < initTotal){
                    initTotal = total;
                    allocationNetEq = netEqEntity;
                }
            }
            if(!StringUtils.isEmpty(allocationNetEq)){
                allocationToNetEq(allocationNetEq,1);
            }
        }catch (Exception e){
            throw  new BusinessException(e,ErrorCodeEnum.SERVER_CODE_ERROR);
        }
        logger.debug("分配的网元服务器:{}",allocationNetEq);
        return allocationNetEq;
    }

    @Override
    public void dynamicReduceCamera(String netEqId) throws BusinessException{
        logger.debug("动态为摄像头删除网元服务器:{}",netEqId);
        NetEqEntity netEqEntity = netEqDao.findNetEqById(netEqId);
        if(netEqEntity == null){
            return;
        }
        allocationToNetEq(netEqEntity,-1);
    }

    public void changeVersion(String netEqId)throws BusinessException{
        logger.debug("修改网元服务器的记录版本:{}",netEqId);
        NetEqEntity netEqEntity = netEqDao.findNetEqById(netEqId);
        if(netEqEntity == null){
            return;
        }
        int currentRecversion = netEqEntity.getCameraRecVersion();
        currentRecversion++;
        netEqEntity.setCameraRecVersion(currentRecversion);
        netEqDao.updateNetEq(netEqEntity);

    }

    private void  allocationToNetEq(NetEqEntity netEqEntity,int type){
        int currentCameraTotal = netEqEntity.getCameraTotal();
        int currentRecversion = netEqEntity.getCameraRecVersion();
        if(type == 1){//增加
            currentCameraTotal++;
        }else if(type == -1 && currentCameraTotal>0){//减少

            currentCameraTotal--;
        }
        currentRecversion++;
        netEqEntity.setCameraTotal(currentCameraTotal);
        netEqEntity.setCameraRecVersion(currentRecversion);
        netEqDao.updateNetEq(netEqEntity);
    }

}
