package com.tcy.smartai.service.service;

import com.tcy.smartai.service.entity.NetEqEntity;
import com.tcy.smartai.service.exception.BusinessException;
import com.tcy.smartai.service.vo.NetEqVo;

import java.util.List;

public interface NetEqService {

    void  registerNetEq(NetEqVo netEqVo)throws BusinessException;

    int  heartbeatNetEq(NetEqVo netEqVo)throws BusinessException;

    NetEqEntity dynamicAllocationCamera() throws BusinessException;

    void dynamicReduceCamera(String netEqId) throws BusinessException;

    public void changeVersion(String netEqId)throws BusinessException;

}
